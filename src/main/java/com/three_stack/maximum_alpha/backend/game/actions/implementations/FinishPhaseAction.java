package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class FinishPhaseAction extends Action {
    @Override
    public void run(State state) {
        super.run(state);
        state.getCurrentPhase().end(state);
        Event event = new Event(getPlayer(state).getUsername() + " has ended " + state.getCurrentPhase().getName());
        state.getEventHistory().add(event);
    }
}
