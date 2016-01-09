package com.three_stack.maximum_alpha.backend.server;

import org.java_websocket.WebSocket;

public class Connection {
	public WebSocket socket;
	public int playerId;
	public int deckId;
	public boolean ready = false;
	
	public Connection(WebSocket socket, int playerId, int deckId) {
		this.socket = socket;
		this.playerId = playerId;
		this.deckId = deckId;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	
	public boolean isReady() {
		return ready;
	}
}
