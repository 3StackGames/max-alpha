package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.List;
import java.util.UUID;

public abstract class MultiTargetPrompt extends Prompt {

    public MultiTargetPrompt(Card source, String prompt, List<UUID> targetables) {
        super(source, prompt);
        this.targetables = targetables;
    }

    private List<UUID> targetables;

    public abstract void resolve(State state, List<Card> targets);
}
