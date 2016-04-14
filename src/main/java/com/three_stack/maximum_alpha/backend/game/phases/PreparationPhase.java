package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class PreparationPhase extends Phase {

    public PreparationPhase() {
        super();
    }


    public void start(State state) {
    }


    public void end(State state) {
        if(state.playersDonePreparing()) {
            state.resetPlayersDonePreparing();
            Player currentPlayer = state.getTurnPlayer();
            currentPlayer.castPreparedSpells(state);
            state.getPlayersExcept(currentPlayer).stream()
                    .forEach(player -> player.castPreparedSpells(state));

            state.setCurrentPhase(DamagePhase.class);
        }
    }

    @Override
    public String getType() {
        return "PREPARATION_PHASE";
    }
}
