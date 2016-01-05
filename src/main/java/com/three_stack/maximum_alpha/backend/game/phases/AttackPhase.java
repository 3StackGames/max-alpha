package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class AttackPhase extends Phase {
    protected static AttackPhase instance;

    protected AttackPhase () {
        super("Attack Phase");
    }

    public static AttackPhase getInstance() {
        if(instance == null) {
            instance = new AttackPhase();
        }
        return instance;
    }

    public void start(State state) {
        state.setCurrentPhase(instance);
    }

    public void end(State state) {
        BlockPhase.getInstance().start(state);
    }
}
