package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.SingleCardEvent;

public class Graveyard extends Zone<Card> {
    public Graveyard(Player owner) {
        super(owner);
    }

    public void add(Card card, State state) {
        super.add(card, state);
        SingleCardEvent event = new SingleCardEvent(owner, "added " + card.getName() + " to their graveyard", card);
        state.addEvent(event);
    }
}
