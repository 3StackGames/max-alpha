package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

public class Hand extends Zone<Card> {
    public Hand(Player owner) {
        super(owner);
    }

    @Override
    public Trigger getOnEnterTrigger() {
        return Trigger.ON_ENTER_HAND;
    }

    @Override
    public Trigger getOnLeaveTrigger() {
        return Trigger.ON_LEAVE_HAND;
    }
}
