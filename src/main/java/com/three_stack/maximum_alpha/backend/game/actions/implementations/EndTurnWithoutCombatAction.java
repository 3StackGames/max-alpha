package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;

public class EndTurnWithoutCombatAction extends Action {
    @Override
    public void run(State state) {
        state.setCombatEnded(true);
        state.createSinglePlayerEvent(player, "end turn without combat", state.getTime(), null);
        state.getCurrentPhase().end(state);
    }

    @Override
    public boolean isValid(State state) {
        boolean notInPrompt = notInPrompt(state);
        boolean correctPhase = isPhase(state, MainPhase.class);
        boolean playerTurn = isPlayerTurn(state);
        boolean combatNotStarted = !state.isCombatEnded();

        return notInPrompt && correctPhase && playerTurn && combatNotStarted;
    }
}
