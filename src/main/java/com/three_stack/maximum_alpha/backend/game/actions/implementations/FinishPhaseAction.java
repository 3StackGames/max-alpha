package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.phases.BlockPhase;

public class FinishPhaseAction extends Action {
    @Override
    public void run(State state) {
        state.createSinglePlayerEvent(player, "end turn", state.getTime(), null);
        state.getCurrentPhase().end(state);
    }
    
    @Override
    public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
		boolean playerTurn = isPhase(state, BlockPhase.class) ? !isPlayerTurn(state) : isPlayerTurn(state);
		
    	return notInPrompt && playerTurn;
    }
}
