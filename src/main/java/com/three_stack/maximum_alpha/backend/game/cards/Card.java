package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.GameEvent;
import com.three_stack.maximum_alpha.backend.game.GameState;
import com.three_stack.maximum_alpha.backend.game.ResourceList;

import java.util.List;
import java.util.Map;

public abstract class Card {
	private final long id;
    private String name;
    private ResourceList cost;
    private String flavorText;

    private List<Runnable> triggers;

    protected Card() {
        id = -1;
	}
	
	/**
	 * Called whenever any GameEvent occurs in a GameState. May or may not trigger another GameEvent in response.
	 * 
	 * @param s The gamestate in which this card exists
	 * @param e The event which may or may not trigger this card's effect(s)
	 * @return The resulting GameEvent corresponding to this card's effect, or null if not triggered
	 */
	public abstract GameEvent effect(GameState s, GameEvent e);

    public void addTrigger(Runnable runnable) {
        triggers.add(runnable);
    }
}
