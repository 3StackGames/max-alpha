package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.UUID;

public abstract class ExistingCardAction extends Action {
    protected UUID cardId;

    protected Card card;

    public UUID getCardId() {
        return cardId;
    }

    public void setCardId(UUID cardId) {
        this.cardId = cardId;
    }

    @Override
    public void setup(State state) {
        super.setup(state);
        card = state.findCard(cardId);
    }
}
