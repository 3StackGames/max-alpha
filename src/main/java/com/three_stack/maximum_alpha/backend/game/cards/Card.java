package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.*;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Card {
	private final long id;
    private String name;
    private ResourceList cost;
    private String flavorText;
    private List<Counter> counters;

    private Map<GameState.Breakpoint, Trigger> triggers;

    protected Card() {
        id = -1;
        triggers = new HashMap<>();
	}
	
	/**
	 * Called whenever any GameEvent occurs in a GameState. May or may not trigger another GameEvent in response.
	 * 
	 * @param state The gamestate in which this card exists
	 * @param event The event which may or may not trigger this card'state effect(state)
	 * @return The resulting GameEvent corresponding to this card'state effect, or null if not triggered
	 */
	public abstract GameEvent effect(GameState state, GameEvent event);

    public void addTrigger(GameState.Breakpoint breakpoint, Trigger trigger) {
        triggers.put(breakpoint, trigger);
    }
}
