package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.UUID;

public abstract class ExistingPairAction extends ExistingCardAction {
    protected UUID targetId;

    protected Card targetCard;
    @Override
    public void run(State state) {
        super.run(state);
        targetCard = state.findCard(targetId);
    }
}
