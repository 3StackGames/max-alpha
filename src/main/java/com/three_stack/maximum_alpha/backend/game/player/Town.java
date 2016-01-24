package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.SingleCardOutcome;

import java.util.UUID;

public class Town extends Zone<Creature> {
    public Town(Player owner) {
        super(owner);
    }

    public void add(Creature creature, State state) {
        super.add(creature, state);
        state.createEventWithSingleCardOutcome(creature, "enter town", null);
    }

    public Creature takeCard(UUID creatureId, State state) {
        Creature creature = super.takeCard(creatureId);
        state.createEventWithSingleCardOutcome(creature, "leave town", null);
        return creature;
    }
}
