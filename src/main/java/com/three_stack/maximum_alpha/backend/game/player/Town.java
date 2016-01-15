package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.SingleCardEvent;

import java.util.UUID;

public class Town extends Zone<Creature> {
    public Town(Player owner) {
        super(owner);
    }

    public void add(Creature creature, State state) {
        super.add(creature, state);
        SingleCardEvent event = new SingleCardEvent(owner, "added " + creature.getName() + " to their town", creature);
        state.addEvent(event);
    }

    public Creature takeCard(UUID creatureId, State state) {
        Creature creature = super.takeCard(creatureId);
        SingleCardEvent event = new SingleCardEvent(owner, "took " + creature.getName() + " from their town", creature);
        state.addEvent(event);
        return creature;
    }
}
