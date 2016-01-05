package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;

public class EndTurnWithoutCombatAction extends Action {
    @Override
    public void run(State state) {
		state.setCombatEnded(true);
		MainPhase.getInstance().end(state);
    }
}
