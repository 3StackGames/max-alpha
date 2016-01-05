package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;

public class BlockPhase extends Phase {
    protected static BlockPhase instance;

    protected BlockPhase() {
        super("Attack Phase");
    }

    public static BlockPhase getInstance() {
        if(instance == null) {
            instance = new BlockPhase();
        }
        return instance;
    }

    public void start(State state) {
        state.setCurrentPhase(instance);
    }

    public void end(State state) {
        FubarPhase.getInstance().start(state);
    }
}
