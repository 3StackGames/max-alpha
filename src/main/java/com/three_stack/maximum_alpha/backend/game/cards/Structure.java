package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.tags.Tag;

public class Structure extends NonSpellCard {
    protected boolean underConstruction;
    protected boolean attackable;

    public Structure(String name, ResourceList cost, String text, String flavorText, int health) {
        super(name, cost, text, flavorText, health);
        underConstruction = true;
        attackable = true;
    }

    public Structure(Structure other) {
        super(other);
        this.underConstruction = other.underConstruction;
    }

    public boolean isUnderConstruction() {
        return underConstruction;
    }

    public void setUnderConstruction(boolean underConstruction) {
        this.underConstruction = underConstruction;
    }

    public boolean isAttackable() {
        return attackable;
    }

    public void setAttackable(boolean attackable) {
        this.attackable = attackable;
    }

    @Override
    public void processTagRemoval(Tag tag) {
        switch (tag.getType()) {
            default:
                break;
        }
    }

    @Override
    public void processTag(Tag tag) {
        switch (tag.getType()) {
            default:
                break;
        }
    }
}
