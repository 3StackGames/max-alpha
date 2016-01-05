package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class PreparationPhase extends Phase {
    protected static PreparationPhase instance;

    protected PreparationPhase() {
        super("Preparation Phase");
    }

    public static PreparationPhase getInstance() {
        if(instance == null) {
            instance = new PreparationPhase();
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
