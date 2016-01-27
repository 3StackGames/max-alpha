package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class AttackPhase extends Phase {
    protected static AttackPhase instance;

    protected AttackPhase () {
        super();
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

    @Override
    public String getType() {
        return "ATTACK_PHASE";
    }
}
