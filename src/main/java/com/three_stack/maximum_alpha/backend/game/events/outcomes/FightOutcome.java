package com.three_stack.maximum_alpha.backend.game.events.outcomes;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.UUID;

public class FightOutcome extends Outcome {
    protected transient Card a;
    protected transient Card b;
    protected int aToBDamage;
    protected int bToADamage;

    public FightOutcome(Card a, Card b, int aToBDamage, int bToADamage) {
        super();
        this.a = a;
        this.b = b;
        this.aToBDamage = aToBDamage;
        this.bToADamage = bToADamage;
    }

    public FightOutcome(SourceDamageTargetsOutcome a, SourceDamageTargetsOutcome b) {

    }

    public int getaToBDamage() {
        return aToBDamage;
    }

    public void setaToBDamage(int aToBDamage) {
        this.aToBDamage = aToBDamage;
    }

    public int getbToADamage() {
        return bToADamage;
    }

    public void setbToADamage(int bToADamage) {
        this.bToADamage = bToADamage;
    }

    public Card getB() {
        return b;
    }

    public void setB(Card b) {
        this.b = b;
    }

    public Card getA() {
        return a;
    }

    public void setA(Card a) {
        this.a = a;
    }

    @ExposeMethodResult("aId")
    public UUID getAId() {
        return a.getId();
    }

    @ExposeMethodResult("bId")
    public UUID getBId() {
        return b.getId();
    }
}
