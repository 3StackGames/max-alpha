package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.GameEvent;
import com.three_stack.maximum_alpha.backend.game.GameState;
import com.three_stack.maximum_alpha.backend.game.ResourceList;

public class Creature extends Card implements Worker{

    public Creature() {

    }

    @Override
    public GameEvent effect(GameState state, GameEvent event) {
        return null;
    }

    @Override
    public ResourceList work(GameState state) {
        return null;
    }
}
