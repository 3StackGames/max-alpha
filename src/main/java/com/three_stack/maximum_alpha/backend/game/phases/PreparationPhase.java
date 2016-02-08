package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class PreparationPhase extends Phase {
    protected static PreparationPhase instance;

    protected PreparationPhase() {
        super();
    }

    public static PreparationPhase getInstance() {
        if(instance == null) {
            instance = new PreparationPhase();
        }
        return instance;
    }

    public void start(State state) {
        state.setCurrentPhase(instance);
    }


    public void end(State state) {
        if(state.playersDonePreparing()) {
            state.resetPlayersDonePreparing();
            Player currentPlayer = state.getTurnPlayer();
            currentPlayer.castPreparedSpells(state);
            state.getPlayersExcept(currentPlayer).stream()
                    .forEach(player -> player.castPreparedSpells(state));

            DamagePhase.getInstance().start(state);
        }
    }

    @Override
    public String getType() {
        return "PREPARATION_PHASE";
    }
}
