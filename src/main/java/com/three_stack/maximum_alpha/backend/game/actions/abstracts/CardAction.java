package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.UUID;

public abstract class CardAction extends Action {
    protected UUID cardId;

    protected Card card;
    /**
     * Auto-Generated Getters and Setters Below
     */
    public UUID getCardId() {
        return cardId;
    }

    public void setCardId(UUID cardId) {
        this.cardId = cardId;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }


}
