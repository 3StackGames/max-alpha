package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public abstract class Effect {
    private Card source;

    private String prompt;

    public abstract String getType();
}
