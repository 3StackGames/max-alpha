package com.three_stack.maximum_alpha.backend.game.cards.instances.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class PilotRecruit extends Creature {
    public PilotRecruit() {
        super("Pilot Recruit", new ResourceList(0,0,0,1,0,0,0), "", "I suck.", 1, 3);
    }
}
