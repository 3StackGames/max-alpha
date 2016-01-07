package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;

public class Structure extends DamageableCard {
    protected boolean underConstruction;

    public Structure(String name, ResourceList cost, String text, String flavorText, int health) {
        super(name, cost, text, flavorText, health);
        underConstruction = true;
    }

    protected Structure(Structure other) {
        super(other);
        this.underConstruction = other.underConstruction;
    }

    public boolean isUnderConstruction() {
        return underConstruction;
    }

    public void setUnderConstruction(boolean underConstruction) {
        this.underConstruction = underConstruction;
    }
}
