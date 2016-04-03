package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

import java.util.List;

public class StructureDeck extends Zone<Structure> {

    public StructureDeck(Player owner) {
        super(owner);
    }

    public StructureDeck(Player owner, List<Structure> cards) {
        super(owner, cards);
    }

    @Override
    public Trigger getOnEnterTrigger() {
        return null;
    }

    @Override
    public Trigger getOnLeaveTrigger() {
        return null;
    }
}
