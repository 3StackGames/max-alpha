package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

import java.util.List;
import java.util.Map;

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
}
