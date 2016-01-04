package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class StartPhase implements Phase {

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
        state.setCurrentPhase(State.Phase.START);
        //@Todo: structure complete
        //resources generated
        state.gatherResources();
        //@Todo: triggers
        //turnPlayerDraw card
        state.turnPlayerDraw();
        //refresh
        state.refreshTurnPlayerCards();
    }

    @Override
    public void end(State state) {

    }
}
