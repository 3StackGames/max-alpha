package com.three_stack.maximum_alpha.backend.game;

import java.util.Map;

public abstract class Card {
	private final long id;
	private int player;
	
	protected Card(long id, int player) {
		this.id = id;
		this.player = player;
	}
	
	/**
	 * Called whenever any GameEvent occurs in a GameState. May or may not trigger another GameEvent in response.
	 * 
	 * @param s The gamestate in which this card exists
	 * @param e The event which may or may not trigger this card's effect(s)
	 * @return The resulting GameEvent corresponding to this card's effect, or null if not triggered
	 */
	public abstract GameEvent effect(GameState s, GameEvent e);
	
	/**
	 * Called each turn at Draw Phase start when this card is assigned as a worker.
	 * 
	 * @param s The gamestate in which this card exists
	 * @return A resource list with resource values equal to the net change in resource quantities
	 */
	public abstract ResourceList work(GameState s);
}
