package com.three_stack.maximum_alpha.backend.game.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public abstract class Step {
    protected final String instruction;

    public Step(String instruction) {
        this.instruction = instruction;
    }

    public abstract void complete(Card input);
}
