package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.*;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

/**
 * Handles common zone operations. Any operations that modify the zone should be marked protected though. Only implementaitons should modify since triggers will occur.
 */
public abstract class Zone<T extends Card> {
    protected transient List<T> cards;
    protected transient Player owner;

    protected Zone(Player owner) {
        setup(owner);
        cards = new ArrayList<>();
    }

    protected void setup(Player owner) {
        this.owner = owner;
    }

    public abstract Trigger getOnEnterTrigger();

    public abstract Trigger getOnLeaveTrigger();

    /**
     *
     * @return Unmodifiable List
     */
    public List<T> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void add(T card, Time time, State state) {
        card.setTimeEnteredZone(time);
        cards.add(card);
        createEnterZoneEvent(card, time, state);
    }

    public void addAll(List<T> cards, Time time, State state) {
        cards.forEach(card -> add(card, time, state));
    }

    public T takeCard(UUID cardId, Time time, State state) {
        Iterator<T> cardIterator = cards.iterator();

        while(cardIterator.hasNext()) {
            T card = cardIterator.next();
            if(card.getId().equals(cardId)) {
                cardIterator.remove();
                createLeaveZoneEvent(card, time, state);
                return card;
            }
        }
        throw new IllegalArgumentException("Card Not Found");
    }

    public boolean remove(T card, Time time, State state) {
        boolean removed = cards.remove(card);
        if(removed) {
            createLeaveZoneEvent(card, time, state);
        }
        return removed;
    }

    public void removeAll(Collection<T> removeCards, Time time, State state) {
        removeCards.forEach(removeCard -> remove(removeCard, time, state));
    }

    protected void createEnterZoneEvent(T card, Time time, State state) {
        String type = "enter " + getClass().getSimpleName();
        state.createSingleCardEvent(card, type, time, getOnEnterTrigger());
    }
    protected void createLeaveZoneEvent(T card, Time time, State state) {
        String type = "leave " + getClass().getSimpleName();
        state.createSingleCardEvent(card, type, time, getOnLeaveTrigger());
    }

    protected T removeWithoutEvent(int index) {
        return cards.remove(index);
    }
    @ExposeMethodResult("cardIds")
    public List<UUID> getCardIds() {
        return cards.stream().map(Card::getId).collect(Collectors.toList());
    }

    public T findCard(UUID cardId) {
        List<T> possibleCards =  cards.stream()
                .filter( card -> card.getId().equals(cardId))
                .collect(Collectors.toList());
        if(!possibleCards.isEmpty()) {
            return possibleCards.get(0);
        } else {
            throw new IllegalArgumentException("Card Not Found");
        }
    }

    protected List<T> subList(int fromIndex, int toIndex) {
        return cards.subList(fromIndex, toIndex);
    }

    //@Todo: Error Handling
    public boolean contains(T card) {
        return cards.contains(card);
    }

    //@Todo: Error Handling
    public boolean containsAll(Collection<T> cards) {
        return cards.containsAll(cards);
    }
}
