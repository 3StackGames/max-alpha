package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class SingleCardEvent extends Event {
    protected Card card;

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
}
