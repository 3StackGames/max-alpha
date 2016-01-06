package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.*;
import java.util.stream.Collectors;

public class Zone {
    protected transient List<Card> cards;
    protected List<UUID> cardIds;

    public Zone() {
        cards = new ArrayList<>();
        cardIds = new ArrayList<>();
    }

    public Zone(List<Card> cards) {
        this.cards = cards;
    }

    /**
     *
     * @return Unmodifiable List
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void add(Card card) {
        this.cards.add(card);
        cardIds.add(card.getId());
    }

    public void addAll(List<Card> cards) {
        this.cards.addAll(cards);
        List<UUID> cardIds = cards.stream().map(Card::getId).collect(Collectors.toList());
        cardIds.addAll(cardIds);
    }

    /**
     *
     * @return Unmodifiable List
     */
    public List<UUID> getCardIds() {
        return Collections.unmodifiableList(cardIds);
    }

    public Card takeCard(UUID cardId) {
        Iterator<Card> cardIterator = cards.iterator();

        while(cardIterator.hasNext()) {
            Card card = cardIterator.next();
            if(card.getId().equals(cardId)) {
                cardIterator.remove();
                cardIds.remove(cardId);
                return card;
            }
        }
        throw new IllegalArgumentException("Card Not Found");
    }
}
