package com.three_stack.maximum_alpha.backend.game.prompts;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class ChoosePrompt extends Prompt {

    public ChoosePrompt(Card source, String prompt, List<Card> options) {
        super(source, prompt);
        this.options = options;
    }

    private List<Card> options;
}
