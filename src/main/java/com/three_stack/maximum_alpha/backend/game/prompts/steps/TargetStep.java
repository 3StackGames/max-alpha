package com.three_stack.maximum_alpha.backend.game.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.Step;

import java.util.List;

public class TargetStep extends Step {
    protected List<Card> targetables;

    protected Card target;

    public TargetStep(String instruction, List<Card> targetables) {
        super(instruction);
        this.targetables = targetables;
    }

    @Override
    public void complete(Card target) {
        setTarget(target);
    }

    public List<Card> getTargetables() {
        return targetables;
    }

    public void setTargetables(List<Card> targetables) {
        this.targetables = targetables;
    }

    public Card getTarget() {
        return target;
    }

    public void setTarget(Card target) {
        this.target = target;
    }
}
