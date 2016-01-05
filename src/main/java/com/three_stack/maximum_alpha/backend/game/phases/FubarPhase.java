package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class FubarPhase extends Phase {
    protected static FubarPhase instance;

    protected FubarPhase () {
        super("Fubar Phase");
    }

    public static FubarPhase getInstance() {
        if(instance == null) {
            instance = new FubarPhase();
        }
        return instance;
    }

    public void start(State state) {
        state.setCurrentPhase(instance);
    }


    public void end(State state) {
        DamagePhase.getInstance().start(state);
    }
}
