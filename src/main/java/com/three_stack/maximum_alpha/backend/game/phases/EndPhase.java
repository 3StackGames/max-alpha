package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class EndPhase extends Phase {
    protected static EndPhase instance;

    protected EndPhase() {
        super();
    }

    public static EndPhase getInstance() {
        if(instance == null) {
            instance = new EndPhase();
        }
        return instance;
    }

    public void start(State state) {
        state.setCurrentPhase(instance);
        state.getTurnPlayer().getField().getCards().forEach(creature -> creature.setHasSummoningSickness(false));
        end(state);
    }

    public void end(State state) {
        state.newTurn();
        StartPhase.getInstance().start(state);
    }

    @Override
    public String getType() {
        return "END_PHASE";
    }
}
