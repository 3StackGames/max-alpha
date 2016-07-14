package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

public abstract class ExistingPairAction extends ExistingCardAction {
    protected UUID targetId;
    protected Card target;

    @Override
    public void setup(State state) {
        super.setup(state);
        target = state.findCard(targetId);
    }
}
