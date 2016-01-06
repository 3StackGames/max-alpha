package com.three_stack.maximum_alpha.backend.game.cards.instances.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class BlueCreature extends Creature {
    public BlueCreature() {
        super("Blue Fish", new ResourceList(ResourceList.Color.BLUE, 1, 0), "", "", 1, 3);
    }
}
