package com.three_stack.maximum_alpha.backend.game;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.ArrayList;
import java.util.UUID;

public class CardList<T extends Card> extends ArrayList<T> {

    public T takeCard(UUID cardId) {
        T target = null;
        for(int i = 0; i < size(); i++) {
            if(get(i).getId().equals(cardId)) {
                target = get(i);
                remove(i);
                return target;
            }
        }
        throw new IllegalArgumentException("Card Nout Found");
    }
}
