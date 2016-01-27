package com.three_stack.maximum_alpha.backend.game.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public abstract class Step {
    /**
     * Used in creation and resolving
     * @Todo: find a better way to do this.
     */
    protected Object value;

    protected final String instruction;

    public Step(String instruction) {
        this.instruction = instruction;
    }

    public Step(String instruction, Object value) {
        this.instruction = instruction;
        this.value = value;
    }

    public abstract void complete(Card input);
    
    public abstract void reset();

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
