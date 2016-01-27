package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public interface InputChecker {
    boolean run(Card input, Prompt prompt);
}
