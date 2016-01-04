package com.three_stack.maximum_alpha.backend.server;

import org.java_websocket.WebSocket;

public class Connection {
	public WebSocket socket;
	public int playerId;
	public int deckId;
	
	public Connection(WebSocket socket, int playerId, int deckId) {
		this.socket = socket;
		this.playerId = playerId;
		this.deckId = deckId;
	}
}
