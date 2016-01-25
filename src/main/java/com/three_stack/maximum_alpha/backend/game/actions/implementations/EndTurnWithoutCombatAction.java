package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.events.PlayerEvent;

public class EndTurnWithoutCombatAction extends Action {
    @Override
    public void run(State state) {
        state.setCombatEnded(true);

        PlayerEvent endTurnEvent = new PlayerEvent(state.getTime(), "end turn without combat", player);
        state.addEvent(endTurnEvent, null);

        state.getCurrentPhase().end(state);
    }

    @Override
    public boolean isValid(State state) {
        boolean notInPrompt = notInPrompt(state);
        boolean correctPhase = isPhase(state, "Main Phase");
        boolean playerTurn = isPlayerTurn(state);
        boolean combatNotStarted = !state.isCombatEnded();

        return notInPrompt && correctPhase && playerTurn && combatNotStarted;
    }
}
