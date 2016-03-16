package com.three_stack.maximum_alpha.backend.game.victories;

import com.three_stack.maximum_alpha.backend.game.State;

public interface VictoryHandler {
	/**
	 * Should set players' status to LOSE, WIN, and TIE as appropriate
	 * @param state
	 */
	public void determineVictory(State state);
}
