package com.three_stack.maximum_alpha.backend.game.victories;

import com.three_stack.maximum_alpha.backend.game.State;

public interface VictoryHandler {
	//True if game is over, false otherwise
	public boolean determineVictory(State state);
}
