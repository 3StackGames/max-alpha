package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.SingleCardEvent;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Hand {
    protected transient Zone<Card> cards;
    protected transient Player owner;

    public Hand(Player owner) {
        this.cards = new Zone<>();
        this.owner = owner;
    }

    public void add(Card card, State state) {
        cards.add(card, state);
        SingleCardEvent event = new SingleCardEvent(owner, "added " + card.getName() + " to their hand", card);
        state.addEvent(event);
        state.notify(Trigger.ON_ENTER_HAND, event);
    }

    /**
     *
     * @return unmodifiable list of cards
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards.getCards());
    }

    /**
     *
     * @return unmodifiable list of card ids
     */
    @ExposeMethodResult("cardIds")
    public List<UUID> getCardIds() {
        return Collections.unmodifiableList(cards.getCardIds());
    }

    public Card takeCard(UUID cardId, State state) {
        Card card = cards.takeCard(cardId);
        SingleCardEvent event = new SingleCardEvent(owner, "removed " + card.getName() + " from their hand", card);
        state.addEvent(event);
        state.notify(Trigger.ON_LEAVE_HAND, event);
        return card;
    }

    public Card findCard(UUID cardId) {
        return cards.findCard(cardId);
    }
}
