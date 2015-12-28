package com.three_stack.maximum_alpha.backend.game;

import java.util.List;

import com.three_stack.maximum_alpha.backend.server.Player;

public class GameParameters {
	public List<Player> players;
	
	public GameParameters(List<Player> players) {
		this.players = players;
	}
}
