package com.three_stack.maximum_alpha.backend.game;

public interface Phase {

    void start(State state);

    void end(State state);
}
