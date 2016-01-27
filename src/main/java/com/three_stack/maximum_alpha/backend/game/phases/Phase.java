package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import io.gsonfire.annotations.ExposeMethodResult;

public abstract class Phase {
    protected Phase(){
    }

    public abstract void start(State state);
    public abstract void end(State state);

    @ExposeMethodResult("type")
    public abstract String getType();
}
