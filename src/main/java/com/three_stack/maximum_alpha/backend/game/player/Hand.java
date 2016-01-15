package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.SingleCardEvent;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;

import java.util.UUID;

public class Hand extends Zone<Card> {
    public Hand(Player owner) {
        super(owner);
    }

    public void add(Card card, State state) {
        super.add(card, state);
        SingleCardEvent event = new SingleCardEvent(owner, "added " + card.getName() + " to their hand", card);
        state.addEvent(event);
        state.notify(Trigger.ON_ENTER_HAND, event);
    }

    public Card takeCard(UUID cardId, State state) {
        Card card = super.takeCard(cardId);
        SingleCardEvent event = new SingleCardEvent(owner, "removed " + card.getName() + " from their hand", card);
        state.addEvent(event);
        state.notify(Trigger.ON_LEAVE_HAND, event);
        return card;
    }
}
