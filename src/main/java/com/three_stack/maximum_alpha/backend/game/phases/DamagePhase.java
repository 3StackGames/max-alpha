package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class DamagePhase extends Phase {
    protected static DamagePhase instance;

    protected DamagePhase () {
        super("Damage Phase");
    }

    public static DamagePhase getInstance() {
        if(instance == null) {
            instance = new DamagePhase();
        }
        return instance;
    }
    public void start(State state) {
        state.setCurrentPhase(instance);
        
        end(state);
    }

    public void end(State state) {
        MainPhase.getInstance().start(state);
    }
}
