package com.three_stack.maximum_alpha.backend.game.effects;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class ChooseEffect extends Effect {

    public ChooseEffect(Card source, String prompt, List<Card> options) {
        super(source, prompt);
        this.options = options;
    }

    private List<Card> options;
}
