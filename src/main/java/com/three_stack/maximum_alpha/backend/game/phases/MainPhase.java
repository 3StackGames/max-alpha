package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class MainPhase extends Phase {
    public MainPhase() {
        super();
    }

    public void start(State state) {
    }

    public void end(State state) {
        if(state.isCombatEnded()) {
            state.setCurrentPhase(new EndPhase());
        }
        else {
            state.setCurrentPhase(new AttackPhase());
        }
    }

    @Override
    public String getType() {
        return "MAIN_PHASE";
    }
}
