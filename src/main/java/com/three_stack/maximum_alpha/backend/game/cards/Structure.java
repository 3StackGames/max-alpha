package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;

public class Structure extends DamageableCard {
    protected Structure(String name, ResourceList cost, String text, String flavorText, int health) {
        super(name, cost, text, flavorText, health);
    }
}
