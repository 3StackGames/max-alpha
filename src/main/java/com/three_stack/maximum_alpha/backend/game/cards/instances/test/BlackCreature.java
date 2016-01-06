package com.three_stack.maximum_alpha.backend.game.cards.instances.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class BlackCreature extends Creature {
    public BlackCreature() {
        super("Black Thinker", new ResourceList(ResourceList.Color.BLACK, 1, 0), "", "", 1, 3);
    }
}
