package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public abstract class Phase {
    protected final String name;

    protected Phase(String name){
        this.name = name;
    }

    public abstract void start(State state);
    public abstract void end(State state);

    public String getName() {
        return name;
    }
}
