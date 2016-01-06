package com.three_stack.maximum_alpha.backend.game;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

public abstract class Trigger {
    protected Card source;

    abstract protected void run(State state, Event event);

    public Card getSource() {
        return source;
    }

    public void setSource(Card source) {
        this.source = source;
    }
}
