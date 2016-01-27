package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public abstract class Phase {
    protected final String type;

    protected Phase(String type){
        this.type = type;
    }

    public abstract void start(State state);
    public abstract void end(State state);

    public String getType() {
        return type;
    }
}
