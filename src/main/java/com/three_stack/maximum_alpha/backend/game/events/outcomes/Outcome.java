package com.three_stack.maximum_alpha.backend.game.events.outcomes;

public abstract class Outcome {
    protected String type;

    protected Outcome() {

    }

    protected Outcome(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
