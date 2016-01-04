package com.three_stack.maximum_alpha.backend.game.effects;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class ChooseEffect extends Effect {
    private List<Card> options;

    @Override
    public String getType() {
        return "CHOOSE";
    }
}
