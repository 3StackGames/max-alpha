package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.SingleCardOutcome;

public class Graveyard extends Zone<Card> {
    public Graveyard(Player owner) {
        super(owner);
    }

    public void add(Card card, State state) {
        super.add(card, state);
        state.createEventWithSingleCardOutcome(card, "enter graveyard", null);
    }
}
