package com.three_stack.maximum_alpha.backend.server;

import org.java_websocket.WebSocket;

public class Player {
	public WebSocket socket;
	public int pid;
	public int did;
	
	public Player(WebSocket socket, int pid, int did) {
		this.socket = socket;
		this.pid = pid;
		this.did = did;
	}
}
