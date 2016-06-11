package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.player.ResourceList;

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
        this.attackable = other.attackable;
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
    public void removeTag(Tag tag) {
        super.removeTag(tag);
        switch (tag.getType()) {
            default:
                break;
        }
    }

    @Override
    public void addTag(Tag tag) {
        super.addTag(tag);
        switch (tag.getType()) {
            default:
                break;
        }
    }
}
