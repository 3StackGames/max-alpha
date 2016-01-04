package com.three_stack.maximum_alpha.backend.game.cards.instances.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class FieldCleric extends Creature {
    public FieldCleric() {
        super("Field Cleric", new ResourceList(2), "", "Trained by Clara Barton", 2, 4);
    }
}
