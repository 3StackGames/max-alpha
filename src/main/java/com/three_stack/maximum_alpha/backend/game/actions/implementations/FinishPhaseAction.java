package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.Outcome;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.PlayerOutcome;

public class FinishPhaseAction extends Action {
    @Override
    public void run(State state) {
        Event event = new Event();
        Outcome outcome = new PlayerOutcome("end " + state.getCurrentPhase().getName(), player);
        event.addOutcome(outcome);
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
