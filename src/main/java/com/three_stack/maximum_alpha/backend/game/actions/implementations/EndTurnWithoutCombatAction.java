package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;

public class EndTurnWithoutCombatAction extends Action {
    @Override
    public void run(State state) {
        MainPhase currentPhase = (MainPhase) state.getCurrentPhase();
        currentPhase.setCombatEnded(true);
        state.createSinglePlayerEvent(player, "end turn without combat", state.getTime(), null);
        state.getCurrentPhase().end(state);
    }

    @Override
    public boolean isValid(State state) {
        boolean notInPrompt = notInPrompt(state);
        boolean correctPhase = state.isPhase(MainPhase.class);
        boolean playerTurn = isPlayerTurn(state);
        MainPhase currentPhase = (MainPhase) state.getCurrentPhase();
        boolean combatNotStarted = !currentPhase.isCombatEnded();

        return notInPrompt && correctPhase && playerTurn && combatNotStarted;
    }
}
