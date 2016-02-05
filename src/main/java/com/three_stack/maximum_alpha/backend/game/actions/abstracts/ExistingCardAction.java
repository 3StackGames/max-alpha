package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import com.three_stack.maximum_alpha.backend.game.State;

public abstract class ExistingCardAction extends CardAction {
    @Override
    public void setup(State state) {
        super.setup(state);
        card = state.findCard(cardId);
    }
}
