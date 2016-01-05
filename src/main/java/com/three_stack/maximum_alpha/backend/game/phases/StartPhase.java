package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;


public class StartPhase extends Phase {
    protected static StartPhase instance;

    protected StartPhase() {
        super("Start Phase");
    }

    public static StartPhase getInstance() {
        if(instance == null) {
            instance = new StartPhase();
        }
        return instance;
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
    public void start(State state) {
        state.setCurrentPhase(instance);
        //@Todo: structure complete
        //resources generated
        state.gatherResources();
        //@Todo: triggers
        //turnPlayerDraw card
        state.turnPlayerDraw();
        //refresh
        state.refreshTurnPlayerCards();

        end(state);
    }

    public void end(State state) {
        MainPhase.getInstance().start(state);
    }


}
