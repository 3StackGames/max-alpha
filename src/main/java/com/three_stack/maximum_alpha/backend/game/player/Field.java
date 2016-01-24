package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.SingleCardOutcome;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;

public  class Field extends Zone<Creature> {
    public Field(Player owner) {
        super(owner);
    }

    public void add(Creature creature, State state) {
        super.add(creature, state);
        state.createEventWithSingleCardOutcome(creature, "enter field", Trigger.ON_ENTER_FIELD);
    }

    public boolean remove(Creature creature) {
        return super.remove(creature);
    }
}
