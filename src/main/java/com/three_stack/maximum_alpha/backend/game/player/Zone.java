package com.three_stack.maximum_alpha.backend.game.player;

import io.gsonfire.annotations.ExposeMethodResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

/**
 * Be careful whenever modifying cards that you also modify cardIds. Ideally call methods rather than modifying it directly.
 */
public class Zone<T extends Card> {
    protected transient List<T> cards;

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

    @ExposeMethodResult("cardIds")
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
