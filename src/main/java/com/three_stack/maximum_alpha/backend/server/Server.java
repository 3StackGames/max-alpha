package com.three_stack.maximum_alpha.backend.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bson.types.ObjectId;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import com.three_stack.maximum_alpha.backend.game.DefaultParameters;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.ActionService;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.utilities.Serializer;

public class Server extends WebSocketServer {

    final int ROOM_SIZE = 2;
    static char code = 'a';
    ExecutorService executor = newBoundedFixedThreadPool(8);

    Map<String, List<Connection>> unstartedGames = new HashMap<>();
    Map<String, State> startedGames = new HashMap<>();
    Runnable matchmaking = new Matchmaking(ROOM_SIZE);
    LinkedBlockingQueue<Connection> pool = new LinkedBlockingQueue<>();
    Collection<Connection> playersInGame = new HashSet<>();

    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 8080;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
        }
        Server server = new Server(port);
        server.start();
        System.out.println("Server started on port: " + server.getPort());
    }

    public static ExecutorService newBoundedFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadPoolExecutor.DiscardPolicy());
    }

    private class HandleRequestRunnable implements Runnable {

        final WebSocket socket;
        final String message;

        public HandleRequestRunnable(WebSocket socket, String message) {
            this.socket = socket;
            this.message = message;
        }

        public void run() {
            try {
                JSONObject json = new JSONObject(message);
                String eventType = json.getString(Message.EVENT_TYPE);
                //todo: add more events
                if (eventType.equals("Find Game")) {
                    synchronized (pool) {
                        System.out.println("adding to matchmaking pool....");
                        ObjectId deckId = new ObjectId(json.getString("deckId"));
                        ObjectId playerId = new ObjectId(json.getString("playerId"));
                        pool.add(new Connection(socket, playerId, deckId));
                        pool.notify();
                    }
                } else if (eventType.equals("Stop Find Game")) {
                	//Should make less jank in the future?
                    synchronized (pool) {
                        ObjectId deckId = new ObjectId(json.getString("deckId"));
                        ObjectId playerId = new ObjectId(json.getString("playerId"));
                        pool.remove(new Connection(socket, playerId, deckId));
                    }
                } else if (eventType.equals("Game Action")) {
                    String gameCode = json.getString("gameCode");
                    String actionName = json.getJSONObject("action").getString("type");
                    Action action = (Action) Serializer.fromJson(json.getJSONObject("action").toString(), ActionService.getAction(actionName));
                    updateGame(gameCode, action, socket);
                } else if (eventType.equals("Player Ready")) {
                    String gameCode = json.getString("gameCode");
                    ObjectId playerId = new ObjectId(json.getString("playerId"));
                	readyGame(gameCode, playerId, json.getBoolean("ready"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendError(socket, "Invalid JSON", message);
            }
        }
    }

    private class Matchmaking implements Runnable {

        int size;

        public Matchmaking(int size) {
            this.size = size;
        }

        public void run() {
            while (true) {
                try {
                    synchronized (pool) {
                        if (pool.size() >= size) {
                            List<Connection> match = new ArrayList<Connection>();
                            for (int i = 0; i < size; i++) {
                                match.add(pool.take());
                            }
                            matchGame(match);
                        }
                        pool.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void matchGame(List<Connection> players) {
    	String gameCode = nextCode();
    	unstartedGames.put(gameCode, players);
        sendGameFound(gameCode);
    }

    public void startGame(String gameCode) {
    	List<Connection> players = unstartedGames.remove(gameCode);
        DefaultParameters parameters = new DefaultParameters(players);
        State newGame = new State(parameters);
        startedGames.put(gameCode, newGame);

        sendStateUpdate(gameCode);
    }
    
    public void readyGame(String gameCode, ObjectId playerId, boolean ready) {
    	List<Connection> players = unstartedGames.get(gameCode);
    	if(!ready) {
    		for(Connection player : players) {
	    		if (!player.playerId.equals(playerId)) {
	    			player.setReady(false);
	    			pool.add(player);
	    		}
	    	}
    		sendGameDeclined(gameCode);
    		unstartedGames.remove(players);
    	}
    	else {
	    	boolean allReady = true;
	    	for(Connection player : players) {
	    		if (player.playerId.equals(playerId)) {
	    			player.setReady(true);    			
	    		}
	    		allReady &= player.isReady();
	    	}
	    	
	    	if(allReady) {
	    		startGame(gameCode);
	    	}
    	}
    }

    public void updateGame(String gameCode, Action action, WebSocket socket) {
    	State state = startedGames.get(gameCode);
        action.setup(state);
        boolean validAction = state.processAction(action);
        if (validAction) {
        	if(state.getCurrentPrompt() != null) {
        		sendPrompt(gameCode);
        	}
        	else {
        		sendStateUpdate(gameCode);
	            if(state.isGameOver()) {
	            	startedGames.remove(gameCode);
	            }
        	}
        } else {
        	sendError(socket, "Invalid Action", ""); //TODO: Get error message from processAction
        }
    }

    //todo: make real code generation
    public static String nextCode() {
        return Character.toString(code++);
    }

    public Server(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public Server(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void start() {
        super.start();
        matchmaking.run();
    }

    //TODO
    public void close() throws IOException, InterruptedException {
    	super.stop();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        sendServerMessage(conn, "you connected! yay");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    	System.out.println("player disconnected");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        executor.submit(new HandleRequestRunnable(conn, message));
        System.out.println(message);
    }

    public void onFragment(WebSocket socket, Framedata fragment) {
        System.out.println("received fragment: " + fragment);
    }

    @Override
    public void onError(WebSocket socket, Exception e) {
        e.printStackTrace();
        if (socket != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    /**
     * Sends <var>text</var> to all currently connected WebSocket clients.
     *
     * @param text The String to send across the network.
     * @throws InterruptedException When socket related I/O errors occur.
     */
    public void sendToAll(String text) {
        Collection<WebSocket> sockets = connections();
        synchronized (sockets) {
            for (WebSocket socket : sockets) {
                socket.send(text);
            }
        }
    }

    public void sendToAll(String text, Collection<Connection> connections) {
        synchronized (connections) {
            for (Connection connection : connections) {
                connection.socket.send(text);
            }
        }
    }

    public void sendError(WebSocket socket, String code, String message) {
        Message error = new Message("Error");
        error.add("code", code);
        error.add("message", message);
        socket.send(error.toString());
    }

    public void sendStateUpdate(String gameCode) {
    	State game = startedGames.get(gameCode); 	
    	String state = game.toString();
        
    	List<Player> players = game.getAllPlayers();
        for (Player player : players) {     	
            Message stateUpdate = new Message("State Update");
            stateUpdate.add("state", new JSONObject(state));
            stateUpdate.add("gameCode", gameCode);
            stateUpdate.add("cardList", new JSONObject(Serializer.toJson(game.generateVisibleCardList(player))));
            JSONObject currentPlayer = new JSONObject();
            currentPlayer.put("playerId", player.getPlayerId());
            currentPlayer.put("playerIndex", players.indexOf(player));
            stateUpdate.add("currentPlayer", currentPlayer);
            
            player.getConnection().socket.send(stateUpdate.toString());
        }
    }

    public void sendServerMessage(WebSocket socket, String serverMessage) {
        Message message = new Message("Server Message");
        message.add("message", serverMessage);
        socket.send(message.toString());
    }
    
    public void sendGameFound(String gameCode) {
        for (Connection player : unstartedGames.get(gameCode)) {
        	Message gameFound = new Message("Game Found");
        	gameFound.add("gameCode", gameCode);
            player.socket.send(gameFound.toString());
        }
    }
    
    public void sendGameDeclined(String gameCode) {
        for (Connection player : unstartedGames.get(gameCode)) {
        	Message gameFound = new Message("Game Declined");
            player.socket.send(gameFound.toString());
        }
    }
    
    public void sendPrompt(String gameCode) {
    	State game = startedGames.get(gameCode); 
    	Prompt prompt = game.getCurrentPrompt();
    	Message promptMessage = new Message("Player Prompt");
    	promptMessage.add("gameCode", gameCode);
        //@Todo: consider renaming to just prompt. unless steps come back
    	promptMessage.add("prompt", new JSONObject(Serializer.toJson(prompt)));
    	prompt.getPlayer().getConnection().socket.send(promptMessage.toString());
    }
}