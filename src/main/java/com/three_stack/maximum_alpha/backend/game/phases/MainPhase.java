package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class MainPhase extends Phase {
    protected static MainPhase instance;

    protected MainPhase() {
        super();
    }

    public static MainPhase getInstance() {
        if(instance == null) {
            instance = new MainPhase();
        }
        return instance;
    }

    public void start(State state) {
        state.setCurrentPhase(instance);
    }

    public void end(State state) {
        if(state.isCombatEnded()) {
            EndPhase.getInstance().start(state);
        }
        else {
            AttackPhase.getInstance().start(state);
        }
    }

    @Override
    public String getType() {
        return "MAIN_PHASE";
    }
}
