package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Be careful whenever modifying cards that you also modify cardIds. Ideally call methods rather than modifying it directly.
 */
public class Zone<T extends Card> {
    protected transient List<T> cards;
    private List<UUID> cardIds; // for gson use only

    public Zone() {
        cards = new ArrayList<>();
    }

    public Zone(List<T> cards) {
        this.cards = new ArrayList<>(cards);
    }

    /**
     *
     * @return Unmodifiable List
     */
    public List<T> getCards() {
        return cards;
    }

    public void add(T newCard) {
        cards.add(newCard);
    }

    public void addAll(Collection<T> newCards) {
        cards.addAll(newCards);
    }

    /**
     *
     * @return Unmodifiable List
     */
    public List<UUID> getCardIds() {
        return cards.stream().map(Card::getId).collect(Collectors.toList());
    }

    public T takeCard(UUID cardId) {
        Iterator<T> cardIterator = cards.iterator();

        while(cardIterator.hasNext()) {
            T card = cardIterator.next();
            if(card.getId().equals(cardId)) {
                cardIterator.remove();
                return card;
            }
        }
        throw new IllegalArgumentException("Card Not Found");
    }

    public void remove(T card) {
        cards.remove(card);
    }

    public T remove(int index) {
        T card = cards.remove(0);
        return card;
    }

    public void removeAll(Collection<T> removeCards) {
        cards.removeAll(removeCards);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return cards.subList(fromIndex, toIndex);
    }

    //@Todo: Error Handling
    public boolean containsAll(T needle) {
        return cards.contains(needle);
    }

    //@Todo: Error Handling
    public boolean containsAll(Collection<T> needles) {
        return cards.containsAll(needles);
    }
}
