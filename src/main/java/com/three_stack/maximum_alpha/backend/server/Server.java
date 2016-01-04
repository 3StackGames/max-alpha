package com.three_stack.maximum_alpha.backend.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import com.three_stack.maximum_alpha.backend.game.Game;
import com.three_stack.maximum_alpha.backend.game.GameAction;
import com.three_stack.maximum_alpha.backend.game.GameParameters;
import com.three_stack.maximum_alpha.backend.game.GameState;

public class Server extends WebSocketServer {
	
	final int ROOM_SIZE = 2;
	static char code = 'a';	
	ExecutorService executor = newBoundedFixedThreadPool(8);
	
	Map<String, GameState> gameStates = new HashMap<String, GameState>();
	Runnable matchmaking = new Matchmaking(ROOM_SIZE);
	LinkedBlockingQueue<Player> pool = new LinkedBlockingQueue<Player>();
	Collection<Player> playersInGame = new HashSet<Player>();
	
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
				else if(type.equals("find game")) 
				{
					synchronized(pool) {
						System.out.println("adding to pool....");
						pool.add(new Player(socket, json.getInt("pid"), json.getInt("did")));
						System.out.println("added");
						pool.notify();
					}
				} 
				else if (type.equals("stop finding")) 
				{
					synchronized(pool) {
						pool.remove(new Player(socket, json.getInt("pid"), json.getInt("did")));
					}
				} 
				else if (type.equals("action")) 
				{
					System.out.println("action: "+json.getString("action"));
					String gameCode = json.getString("gameCode");
					GameState game = gameStates.get(gameCode);
					GameAction action = Game.stringToAction(json.getString("action"));
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
							List<Player> match = new ArrayList<Player>();
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
	
	public void createGame(List<Player> players) {
		GameParameters gp = new GameParameters(players);
		GameState newGame = new GameState(gp);
		gameStates.put(nextCode(), newGame);
		
		sendToAll("game created", players);
	}

	public void updateGame(String gameCode, GameState game, GameAction action) {
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

	public void onFragment( WebSocket conn, Framedata fragment ) {
		System.out.println( "received fragment: " + fragment );
	}

	public static void main( String[] args ) throws InterruptedException , IOException {
		int port = 8080;
		try {
			port = Integer.parseInt( args[ 0 ] );
		} catch ( Exception ex ) {
		}
		Server s = new Server( port );
		s.start();
		System.out.println( "Server started on port: " + s.getPort() );

		/*BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while ( true ) {
			String in = sysin.readLine();
			//s.sendToAll( in );
			if( in.equals( "exit" ) ) {
				s.stop();
				break;
			} else if( in.equals( "restart" ) ) {
				s.stop();
				s.start();
				break;
			}
		}*/
	}
	
	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
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
		Collection<WebSocket> con = connections();
		synchronized ( con ) {
			for( WebSocket s : con ) {
				s.send( text );
			}
		}
	}

	public void sendToAll( String text , Collection<Player> con ) {
		synchronized ( con ) {
			for( Player p : con ) {
				p.socket.send( text );
			}
		}
	}
	
	public void sendToGame(GameState game) {
		for (Player p : game.players) {
			p.socket.send(game.toString());
		}
	}
}