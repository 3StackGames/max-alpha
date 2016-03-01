package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

public  class Field extends NonSpellCardZone<Creature> {
    public Field(Player owner) {
        super(owner);
    }

    @Override
    public Trigger getOnEnterTrigger() {
        return Trigger.ON_ENTER_FIELD;
    }

    @Override
    public Trigger getOnLeaveTrigger() {
        return Trigger.ON_LEAVE_FIELD;
    }
}