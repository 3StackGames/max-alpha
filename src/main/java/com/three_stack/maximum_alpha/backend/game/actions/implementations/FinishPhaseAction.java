package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.events.PlayerEvent;

public class FinishPhaseAction extends Action {
    @Override
    public void run(State state) {
        PlayerEvent endTurnEvent = new PlayerEvent(state.getTime(), "end turn", player);
        state.addEvent(endTurnEvent, null);

        state.getCurrentPhase().end(state);
    }
    
    @Override
    public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
		boolean playerTurn = isPhase(state, "Block Phase") ? !isPlayerTurn(state) : isPlayerTurn(state);
		
    	return notInPrompt && playerTurn;
    }
}
