package com.three_stack.maximum_alpha.backend.server;

import java.util.List;
import java.util.Vector;

public class Matchmaking implements Runnable {
	
	private Vector<Player> pool;
	private int MAX_ROOM_SIZE;
	
	public Matchmaking(Vector<Player> pool, int size) {
		this.pool = pool; //shallow copy so that Server.java can modify the pool and vice versa
		MAX_ROOM_SIZE = size;
	}
	
	public void run() {
		//do stuff with pool
	}
	
	//Reports a match found to main thread, which then creates the game and sends it off
	private void report(List<Player> players) {
	}
}
