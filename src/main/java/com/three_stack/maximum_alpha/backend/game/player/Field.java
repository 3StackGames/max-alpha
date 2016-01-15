package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;

import java.util.Collections;
import java.util.List;

public class Field {
    protected Zone<Creature> creatures;

    public Field() {
        creatures = new Zone<>();
    }

    public void add(Creature creature, State state, Event event) {
        creatures.add(creature, state);
        state.notify(Trigger.ON_ENTER_FIELD, event);
    }

    /**
     *
     * @return unmodifiable list of creatures
     */
    public List<Creature> getCreatures() {
        return Collections.unmodifiableList(creatures.getCards());
    }
}
