package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.UUID;

public class Effect {
    protected UUID id;
    protected Card source;
    protected Check check;
    protected Result result;
    protected Object value;

    public Effect(Card source, Check check, Result result, Object value) {
        id = UUID.randomUUID();
        this.source = source;
        this.check = check;
        this.result = result;
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Card getSource() {
        return source;
    }

    public void setSource(Card source) {
        this.source = source;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
