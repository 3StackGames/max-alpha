package com.three_stack.maximum_alpha.backend.game.effects.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public interface OptionContainer {
    boolean isValidOption(Card needle);

    void removeOption(Card card);

    boolean isOptionSelected();
}
