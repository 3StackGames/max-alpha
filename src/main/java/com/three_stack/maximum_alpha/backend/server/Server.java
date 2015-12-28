package com.three_stack.maximum_alpha.backend.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
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
import com.three_stack.maximum_alpha.backend.game.GameParameters;
import com.three_stack.maximum_alpha.backend.game.GameState;

public class Server extends WebSocketServer {
	
	final int MAX_ROOM_SIZE = 2;
	static char code = 'a';
	
	Vector<Player> waitingPlayers = new Vector<Player>(); //vector for synchronization
	Map<String, GameState> gameStates = new HashMap<String, GameState>();
	
	Queue<Player> matchPool = new LinkedBlockingQueue<Player>();
	
	ExecutorService executor = newBoundedFixedThreadPool(8);
	
	public static ExecutorService newBoundedFixedThreadPool(int nThreads) {
		return new ThreadPoolExecutor(nThreads, nThreads,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(),
				new ThreadPoolExecutor.DiscardPolicy());
	}

	public class HandleRequestRunnable implements Runnable {

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
				//todo: make real matchmaking (see Matchmaking.java)
				if(type.equals("find game")) {
					waitingPlayers.add(new Player(socket, json.getInt("pid"), json.getInt("did")));
					
					if(waitingPlayers.size() >= MAX_ROOM_SIZE) {
						createGame();
					}
				} else if (type.equals("stop finding")) {
					waitingPlayers.remove(new Player(socket, json.getInt("pid"), json.getInt("did")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				socket.send("Invalid json");
			}
		}
	}
	
	public void createGame() {
		List<Player> players = waitingPlayers.subList(0, MAX_ROOM_SIZE);
		GameParameters gp = new GameParameters(players);
		GameState newGame = Game.newGame(gp);
		gameStates.put(nextCode(), newGame);
		
		sendToAll("game created", players);
		waitingPlayers = (Vector<Player>) waitingPlayers.subList(MAX_ROOM_SIZE, waitingPlayers.size());
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
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		this.sendToAll( "new connection: " + handshake.getResourceDescriptor() );
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

		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
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
		}
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

	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToAll( String text , Collection<Player> con ) {
		synchronized ( con ) {
			for( Player p : con ) {
				p.socket.send( text );
			}
		}
	}
}