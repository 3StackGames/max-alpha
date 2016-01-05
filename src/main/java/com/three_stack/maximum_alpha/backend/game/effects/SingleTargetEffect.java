package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.List;
import java.util.UUID;

public abstract class SingleTargetEffect extends Effect {
    protected List<UUID> targetables;

    public abstract void resolve(State state, Card targets);

    public List<UUID> getTargetables() {
        return targetables;
    }

    public void setTargetables(List<UUID> targetables) {
        this.targetables = targetables;
    }
}
