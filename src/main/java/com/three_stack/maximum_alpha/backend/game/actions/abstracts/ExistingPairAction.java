package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.UUID;

public abstract class ExistingPairAction extends ExistingCardAction {
    protected UUID targetId;

    protected Card target;

    @Override
    public void setup(State state) {
        super.setup(state);
        target = state.findCard(targetId);
    }
}
