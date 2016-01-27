package com.three_stack.maximum_alpha.backend.game.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class ChooseStep extends Step {
    protected transient Card choice;

    public ChooseStep(String instruction) {
        super(instruction);
    }

    @Override
    public void complete(Card input) {
        setChoice(input);
    }

    @Override
    public void reset() {

    }

    public Card getChoice() {
        return choice;
    }

    public void setChoice(Card choice) {
        this.choice = choice;
    }
}
