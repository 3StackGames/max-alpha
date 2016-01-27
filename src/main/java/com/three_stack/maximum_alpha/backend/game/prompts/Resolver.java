package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;

public interface Resolver {
    void run(State state, Prompt prompt);
}
