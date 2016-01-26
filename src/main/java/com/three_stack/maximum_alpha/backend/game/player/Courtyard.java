package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

public class Courtyard extends Zone<Structure> {
    public Courtyard(Player owner) {
        super(owner);
    }

    @Override
    public Trigger getOnEnterTrigger() {
        return Trigger.ON_ENTER_COURTYARD;
    }

    @Override
    public Trigger getOnLeaveTrigger() {
        return Trigger.ON_LEAVE_COURTYARD;
    }
}
