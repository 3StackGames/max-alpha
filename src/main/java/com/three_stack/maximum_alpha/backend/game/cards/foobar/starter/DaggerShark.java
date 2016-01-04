package com.three_stack.maximum_alpha.backend.game.cards.foobar.starter;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class DaggerShark {
    public static Card create() {
        Card card = new Creature();
        Runnable onAttack = new Runnable() {
            @Override
            public void run() {



            }
        };
        card.addTrigger(onAttack);
        return card;
    }


}
