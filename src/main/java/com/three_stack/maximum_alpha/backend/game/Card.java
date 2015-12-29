package com.three_stack.maximum_alpha.backend.game;

public abstract class Card {
	private final long id;
	
	protected Card(long id) {
		this.id = id;
	}
	
	/**
	 * Called whenever any GameEvent occurs in a GameState. May or may not trigger another GameEvent in response.
	 * 
	 * @param s The gamestate in which this card exists
	 * @param e The event which may or may not trigger this card's effect(s)
	 * @return The resulting GameEvent corresponding to this card's effect, or null if not triggered
	 */
	public abstract GameEvent effect(GameState s, GameEvent e);
}
