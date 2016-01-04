package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public interface Phase {

    void start(State state);

    void end(State state);
}
