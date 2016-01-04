package com.three_stack.maximum_alpha.backend.game.cards.foobar;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class CardFactory {

    public Card generateCard(long id) {
        if(id == 3333) {
            return DaggerShark.create();
        } else {
            throw new IllegalArgumentException("Card Not Found");
        }
    }
}
