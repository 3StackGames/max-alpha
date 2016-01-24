package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

public class Graveyard extends Zone<Card> {
    public Graveyard(Player owner) {
        super(owner);
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
