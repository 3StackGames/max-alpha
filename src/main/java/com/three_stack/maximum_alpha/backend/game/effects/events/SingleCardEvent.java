package com.three_stack.maximum_alpha.backend.game.effects.events;

import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.UUID;

public class SingleCardEvent extends Event {
    protected transient Card card;

    public SingleCardEvent(Time time, String type, Card card) {
        super(time, type);
        this.card = card;
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
