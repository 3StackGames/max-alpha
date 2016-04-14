package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class MainPhase extends Phase {
    protected boolean combatEnded;

    public MainPhase() {
        super();
        combatEnded = false;
    }

    public MainPhase(boolean combatEnded) {
        super();
        this.combatEnded = combatEnded;
    }

    public void start(State state) {
    }

    public void end(State state) {
        if(isCombatEnded()) {
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

    public boolean isCombatEnded() {
        return combatEnded;
    }

    public void setCombatEnded(boolean combatEnded) {
        this.combatEnded = combatEnded;
    }
}
