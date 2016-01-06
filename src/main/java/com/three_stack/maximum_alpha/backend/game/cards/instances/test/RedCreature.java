package com.three_stack.maximum_alpha.backend.game.cards.instances.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class RedCreature extends Creature {
    public RedCreature() {
        super("Red Beast", new ResourceList(ResourceList.Color.RED, 1, 0), "", "", 1, 3);
    }
}
