package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.List;

public abstract class SingleTargetPrompt extends Prompt {
    protected List<Card> targetables;

    public SingleTargetPrompt(Card source, String prompt, List<Card> targetables) {
        super(source, prompt);
        this.targetables = targetables;
    }

    public abstract void resolve(State state, Card target);

    public List<Card> getTargetables() {
        return targetables;
    }

    public void setTargetables(List<Card> targetables) {
        this.targetables = targetables;
    }
}
