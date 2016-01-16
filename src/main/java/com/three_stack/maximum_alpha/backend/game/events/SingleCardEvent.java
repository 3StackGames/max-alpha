package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.UUID;

public class SingleCardEvent extends Event {
    protected transient Card card;

    public SingleCardEvent(Player player, String action, Card card) {
        super(player, action);
        this.card = card;
    }

    public SingleCardEvent(Card card, String description) {
        super(description);
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
