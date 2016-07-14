package com.three_stack.maximum_alpha.backend.game.effects.events;

import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class SourceHealTargetEvent extends SourceTargetEvent {
    protected final int heal;
    protected static final String type = "HEAL";

    public SourceHealTargetEvent(Time time, Card source, Card target, int heal) {
        super(time, type, source, target);
        this.heal = heal;
    }

    public int getHeal() {
        return heal;
    }
}
