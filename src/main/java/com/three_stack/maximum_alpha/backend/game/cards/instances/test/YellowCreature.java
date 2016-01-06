package com.three_stack.maximum_alpha.backend.game.cards.instances.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class YellowCreature extends Creature {
    public YellowCreature() {
        super("Yellow Scarab", new ResourceList(ResourceList.Color.YELLOW, 1, 0), "", "", 1, 3);
    }
}
