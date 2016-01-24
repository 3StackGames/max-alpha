package com.three_stack.maximum_alpha.backend.game.events.outcomes;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.Outcome;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.UUID;

public class SingleCardOutcome extends Outcome {
    protected transient Card card;

    public SingleCardOutcome(String type, Card card) {
        super(type);
        this.card = card;
        this.type = type;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @ExposeMethodResult("cardId")
    public UUID getCardId() {
        return card.getId();
    }
}
