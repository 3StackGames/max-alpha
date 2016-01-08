package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.State;

public interface Result {
    void run(State state, Effect effect, Event event, Object value);
}
