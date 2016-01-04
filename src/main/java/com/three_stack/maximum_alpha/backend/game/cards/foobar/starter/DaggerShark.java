package com.three_stack.maximum_alpha.backend.game.cards.foobar.starter;

import com.three_stack.maximum_alpha.backend.game.GameEvent;
import com.three_stack.maximum_alpha.backend.game.GameState;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class DaggerShark {
    public static Card create() {
        Card card = new Creature();
        Trigger attack = new Trigger() {
            @Override
            protected void run(GameState state, GameEvent event) {
                boolean isFirstAttacker = false;
                if(!isFirstAttacker) {
                    return;
                }


            }
        };
        return card;
    }
}
