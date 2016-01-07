package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.Step;

import java.util.ArrayList;
import java.util.List;

public abstract class Prompt {
    protected Card source;
    protected List<Step> steps;
    protected int currentStep;

    public Prompt(Card source) {
        this.source = source;
        this.steps = new ArrayList<>();
        currentStep = 0;
    }

    public void processCurrentStep(Card input) {
        currentStep++;
    }

    public abstract void resolve(State state);

    public boolean isDone() {
        return currentStep >= steps.size();
    }

    public Card getSource() {
        return source;
    }

    public void setSource(Card source) {
        this.source = source;
    }
}
