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

import com.three_stack.maximum_alpha.backend.game.*;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

public class Server extends WebSocketServer {
	
	final int ROOM_SIZE = 2;
	static char code = 'a';	
	ExecutorService executor = newBoundedFixedThreadPool(8);
	
	Map<String, State> gameStates = new HashMap<>();
	Runnable matchmaking = new Matchmaking(ROOM_SIZE);
	LinkedBlockingQueue<Connection> pool = new LinkedBlockingQueue<>();
	Collection<Connection> playersInGame = new HashSet<>();
	
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
				String type = json.getString("type");
				//todo: add more events
				if(type.equals("login")) {
					
				}
				else if(type.equals("Find Game"))
				{
					synchronized(pool) {
						System.out.println("adding to pool....");
						pool.add(new Connection(socket, json.getInt("playerId"), json.getInt("deckId")));
						System.out.println("added");
						pool.notify();
					}
				} 
				else if (type.equals("Stop Find Game"))
				{
					synchronized(pool) {
						pool.remove(new Connection(socket, json.getInt("playerId"), json.getInt("deckId")));
					}
				} 
				else if (type.equals("Game Action"))
				{
					System.out.println("action: "+json.getString("action"));
					String gameCode = json.getString("gameCode");
					State game = gameStates.get(gameCode);
					Action action = Game.stringToAction(json.getString("action"));
					updateGame(gameCode, game, action);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				socket.send("Invalid json");
			}
		}
	}
	
	private class Matchmaking implements Runnable {
		
		int size;
		
		public Matchmaking(int size) {
			this.size = size;
		}
		
		public void run() {
			while(true) {
				try {
					synchronized(pool) {
						if(pool.size() >= size) 
						{				
							System.out.println("in loop");
							List<Connection> match = new ArrayList<Connection>();
							for(int i = 0; i < size; i++) {
								match.add(pool.take());
							}
							createGame(match);
						}
						pool.wait();
					}
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void createGame(List<Connection> players) {
		Parameters parameters = new Parameters(players);
		State newGame = new State(parameters);
		gameStates.put(nextCode(), newGame);
		
		sendToAll("Game Found", players);
	}

	public void updateGame(String gameCode, State game, Action action) {
		if(game.isLegalAction(action)) {
			game.processAction(action);
			
			sendToGame(game);
		}
	}
	
	//todo: make real code generation
	public static String nextCode() {
		return Character.toString(code++);
	}

	public Server( int port ) throws UnknownHostException {
		super( new InetSocketAddress( port ) );
	}

	public Server( InetSocketAddress address ) {
		super( address );
	}
	
	@Override
	public void start() {
		super.start();
		matchmaking.run();
	}
	
	//necessary?
	public void close() {
		
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		this.sendToAll( "new connection");
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		this.sendToAll( conn + " has left the room!" );
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		executor.submit( new HandleRequestRunnable(conn, message) );
		System.out.println(message);
	}

	public void onFragment( WebSocket socket, Framedata fragment ) {
		System.out.println( "received fragment: " + fragment );
	}

	public static void main( String[] args ) throws InterruptedException , IOException {
		int port = 8080;
		try {
			port = Integer.parseInt( args[ 0 ] );
		} catch ( Exception e ) {
		}
		Server server = new Server( port );
		server.start();
		System.out.println( "Server started on port: " + server.getPort() );
	}
	
	@Override
	public void onError( WebSocket socket, Exception e ) {
		e.printStackTrace();
		if( socket != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}
	
	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToAll( String text) {
		Collection<WebSocket> sockets = connections();
		synchronized ( sockets ) {
			for( WebSocket socket : sockets ) {
				socket.send( text );
			}
		}
	}

	public void sendToAll( String text , Collection<Connection> connections ) {
		synchronized ( connections ) {
			for( Connection connection : connections ) {
				connection.socket.send( text );
			}
		}
	}
	
	public void sendToGame(State game) {
		for (Player player : game.players) {
			player.getConnection().socket.send(game.toString());
		}
	}
}