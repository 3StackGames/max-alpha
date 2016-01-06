package com.three_stack.maximum_alpha.backend.game.cards.instances.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class WhiteCreature extends Creature {
    public WhiteCreature() {
        super("White Pilot", new ResourceList(ResourceList.Color.WHITE, 1, 0), "", "", 1, 3);
    }
}
