package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.events.SingleCardEvent;

public class Courtyard extends Zone<Structure> {
    public Courtyard(Player owner) {
        super(owner);
    }

    public void add(Structure structure, State state) {
        super.add(structure, state);
        SingleCardEvent event = new SingleCardEvent(owner, "added " + structure.getName() + " to their courtyard", structure);
        state.addEvent(event);
    }

    public boolean remove(Structure structure) {
        return super.remove(structure);
    }
}
