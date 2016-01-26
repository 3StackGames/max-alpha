package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

public class Town extends Zone<Creature> {
    public Town(Player owner) {
        super(owner);
    }

    @Override
    public Trigger getOnEnterTrigger() {
        return Trigger.ON_ENTER_TOWN;
    }

    @Override
    public Trigger getOnLeaveTrigger() {
        return Trigger.ON_LEAVE_TOWN;
    }
}
