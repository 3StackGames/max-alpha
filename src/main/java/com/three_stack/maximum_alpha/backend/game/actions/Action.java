package com.three_stack.maximum_alpha.backend.game.actions;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.events.Event;

/**
 * Always player Input
 */
public abstract class Action extends Event {
    protected int playerId;

    abstract public void run(State state);

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
