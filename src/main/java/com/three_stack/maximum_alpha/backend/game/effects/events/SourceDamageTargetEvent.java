package com.three_stack.maximum_alpha.backend.game.effects.events;

import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class SourceDamageTargetEvent extends SourceTargetEvent {
    protected int damage;
    protected static final String type = "DAMAGE";

    public SourceDamageTargetEvent(Time time, Card source, Card target, int damage) {
        super(time, type, source, target);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
