package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.phases.BlockPhase;
import com.three_stack.maximum_alpha.backend.game.phases.PreparationPhase;

public class FinishPhaseAction extends Action {
    @Override
    public void run(State state) {
        state.createSinglePlayerEvent(player, "end turn", state.getTime(), null);
        if(state.isPhase(PreparationPhase.class)) {
            player.setPreparationDone(true);
        }
        state.getCurrentPhase().end(state);
    }
    
    @Override
    public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
        if(state.isPhase(BlockPhase.class) && isPlayerTurn(state)) {
            return false;
        } else if(state.isPhase(PreparationPhase.class) && player.isPreparationDone()) {
            return false;
        }
		
    	return notInPrompt;
    }
}
