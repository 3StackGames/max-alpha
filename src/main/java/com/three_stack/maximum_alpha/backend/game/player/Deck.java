package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Deck<T extends Card> extends Zone<T> {
    public Deck() {
        super();
    }

    public Deck(List<T> cards) {
        super(cards);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        return remove(0);
    }

    public List<T> drawCards(int numCards) {
        List<T> subDeck = subList(0, numCards);
        removeAll(subDeck);
        return subDeck;
    }

    public List<T> topCards(int numCards) {
        return subList(0, numCards);
    }

    public void placeAtBottom(T card) {
        if (containsAll(card)) {
            remove(card);
            add(card);
        }
    }

    public void placeAtBottom(Collection<T> bottomCards) {
        if (containsAll(bottomCards)) {
            removeAll(bottomCards);
            addAll(bottomCards);
        }
    }

    public static Deck<Card> loadDeck(int deckId) {
        List<Card> cards = new ArrayList<>();

        for(int i = 0; i < 7; i++) {
            cards.add(new WhiteCreature());
            cards.add(new BlackCreature());
            cards.add(new YellowCreature());
            cards.add(new RedCreature());
            cards.add(new BlueCreature());
            cards.add(new GreenCreature());
            cards.add(new MilitiaMinuteman());
        }

        return new Deck<>(cards);
    }
}
