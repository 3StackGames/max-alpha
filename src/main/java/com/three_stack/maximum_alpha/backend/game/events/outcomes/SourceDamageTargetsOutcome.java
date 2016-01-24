package com.three_stack.maximum_alpha.backend.game.events.outcomes;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.List;

public class SourceDamageTargetsOutcome extends SourceTargetsOutcome {
    protected int damage;

    public SourceDamageTargetsOutcome(Card source, List<Card> targets, int damage) {
        super(source, targets);
        this.damage = damage;
    }

    public SourceDamageTargetsOutcome(Card source, Card target, int damage) {
        super(source, target);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
