package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.SingleCardOutcome;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;

import java.util.UUID;

public class Hand extends Zone<Card> {
    public Hand(Player owner) {
        super(owner);
    }

    public void add(Card card, State state) {
        super.add(card, state);
        state.createEventWithSingleCardOutcome(card, "add to hand", Trigger.ON_ENTER_HAND);
    }

    public Card takeCard(UUID cardId, State state) {
        Card card = super.takeCard(cardId);
        state.createEventWithSingleCardOutcome(card, "remove from hand", Trigger.ON_LEAVE_HAND);
        return card;
    }
}
