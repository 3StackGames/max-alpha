package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;

public interface Resolver {
    void run(Event event, State state, Prompt prompt);
}
