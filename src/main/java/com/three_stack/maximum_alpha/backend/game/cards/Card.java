package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.Counter;
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
    private List<Counter> counters;

    private List<Runnable> triggers;

    protected Card() {
        id = -1;
	}
	
	/**
	 * Called whenever any GameEvent occurs in a GameState. May or may not trigger another GameEvent in response.
	 * 
	 * @param state The gamestate in which this card exists
	 * @param event The event which may or may not trigger this card'state effect(state)
	 * @return The resulting GameEvent corresponding to this card'state effect, or null if not triggered
	 */
	public abstract GameEvent effect(GameState state, GameEvent event);

    public void addTrigger(Runnable runnable) {
        triggers.add(runnable);
    }
}
