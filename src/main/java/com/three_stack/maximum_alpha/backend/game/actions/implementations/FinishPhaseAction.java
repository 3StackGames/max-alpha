package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class FinishPhaseAction extends Action {
    @Override
    public void run(State state) {
        super.run(state);
        Event event = new Event(getPlayer(state).getUsername() + " has ended " + state.getCurrentPhase().getName());
        state.addEvent(event);
        state.getCurrentPhase().end(state);
    }
    
    @Override
    public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
		boolean playerTurn = isPhase(state, "Block Phase") ? !isPlayerTurn(state) : isPlayerTurn(state);
		
    	return notInPrompt && playerTurn;
    }
}
