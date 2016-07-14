package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;


public class StartPhase extends Phase {

    public StartPhase() {
        super();
    }

    /**
     * Overview
     * 1. Structures Complete
     * 2. Resources Generated
     * 3. Begin Turn Triggers
     * 4. Draw Card
     * 5. Refresh Cards
     * @param state
     */
    @Override
    public void start(State state) {
        state.completeStructures();
        //resources generated
        state.gatherResources();
        //@TODO: triggerEffects
        //begin turn triggers
        Event startPhaseStartEvent = new Event(state.getTime(), "START PHASE START");
        state.addEvent(startPhaseStartEvent, Trigger.ON_START_PHASE_START);
        //turnPlayerDraw a
        state.turnPlayerDraw();
        //refresh
        state.refreshTurnPlayerCards(state.getTime(), state);

        state.getTurnPlayer().setHasAssignedOrPulled(false);

        end(state);
    }

    @Override
    public void end(State state) {
        state.setCurrentPhase(MainPhase.class);
    }

    @Override
    public String getType() {
        return "START_PHASE";
    }
}
