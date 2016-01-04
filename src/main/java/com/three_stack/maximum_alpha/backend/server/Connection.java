package com.three_stack.maximum_alpha.backend.server;

import org.java_websocket.WebSocket;

public class Connection {
	public WebSocket socket;
	public int pid;
	public int did;
	
	public Connection(WebSocket socket, int pid, int did) {
		this.socket = socket;
		this.pid = pid;
		this.did = did;
	}
}
