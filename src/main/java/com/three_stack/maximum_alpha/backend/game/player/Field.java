package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.events.SingleCardEvent;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;

import java.util.Collections;
import java.util.List;

public class Field extends Zone<Creature> {
    public Field(Player owner) {
        super(owner);
    }

    public void add(Creature creature, State state) {
        super.add(creature, state);
        SingleCardEvent event = new SingleCardEvent(owner, "added " + creature.getName() + " to their field", creature);
        state.addEvent(event);
        state.notify(Trigger.ON_ENTER_FIELD, event);
    }
}
