package com.three_stack.maximum_alpha.backend.game.cards.instances.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class GreenCreature extends Creature {
    public GreenCreature() {
        super("Green Tree", new ResourceList(ResourceList.Color.GREEN, 1, 0), "", "", 1, 3);
    }
}
