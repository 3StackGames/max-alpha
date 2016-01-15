package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.*;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

/**
 * Be careful whenever modifying cards that you also modify cardIds. Ideally call methods rather than modifying it directly.
 */
public class Zone<T extends Card> {
    protected transient List<T> cards;
    protected transient Player owner;

    protected Zone(Player owner) {
        setup(owner);
        cards = new ArrayList<>();
    }

    protected Zone(List<T> cards, Player owner) {
        setup(owner);
        this.cards = new ArrayList<>(cards);
    }

    protected void setup(Player owner) {
        this.owner = owner;
    }

    /**
     *
     * @return Unmodifiable List
     */
    protected List<T> getCards() {
        return Collections.unmodifiableList(cards);
    }

    protected void add(T newCard, State state) {
        newCard.setTimeEnteredZone(state.getTime());
        cards.add(newCard);
    }

    @ExposeMethodResult("cardIds")
    protected List<UUID> getCardIds() {
        return cards.stream().map(Card::getId).collect(Collectors.toList());
    }

    protected T takeCard(UUID cardId) {
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

    protected T findCard(UUID cardId) {
        return cards.stream()
                .filter( card -> card.getId().equals(cardId))
                .collect(Collectors.toList())
                .get(0);
    }

    protected void remove(T card) {
        cards.remove(card);
    }

    protected T remove(int index) {
        T card = cards.remove(index);
        return card;
    }

    protected void removeAll(Collection<T> removeCards) {
        cards.removeAll(removeCards);
    }

    protected List<T> subList(int fromIndex, int toIndex) {
        return cards.subList(fromIndex, toIndex);
    }

    //@Todo: Error Handling
    protected boolean contains(T card) {
        return cards.contains(card);
    }

    //@Todo: Error Handling
    protected boolean containsAll(Collection<T> cards) {
        return cards.containsAll(cards);
    }
}
