package com.three_stack.maximum_alpha.backend.game.effects.events;

import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

import io.gsonfire.annotations.ExposeMethodResult;

public class SingleCardEvent extends Event {
    protected transient final Card card;

    public SingleCardEvent(Time time, String type, Card card) {
        super(time, type);
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    @ExposeMethodResult("cardId")
    public UUID getCardId() {
        return card.getId();
    }
}
