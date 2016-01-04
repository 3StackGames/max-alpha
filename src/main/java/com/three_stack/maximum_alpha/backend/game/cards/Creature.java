package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.GameEvent;
import com.three_stack.maximum_alpha.backend.game.GameState;
import com.three_stack.maximum_alpha.backend.game.ResourceList;

public class Creature extends Card implements Worker{

    public Creature() {

    }

    @Override
    public GameEvent effect(GameState s, GameEvent e) {
        return null;
    }

    @Override
    public ResourceList work(GameState s) {
        return null;
    }
}
