package com.three_stack.maximum_alpha.backend.game.prompts;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

//@Todo: Do this
public abstract class ChoosePrompt extends Prompt {

    public ChoosePrompt(Card source, List<Card> options) {
        super(source);
        this.options = options;
    }

    private List<Card> options;
}
