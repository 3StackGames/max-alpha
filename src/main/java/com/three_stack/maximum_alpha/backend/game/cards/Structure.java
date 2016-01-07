package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;

public abstract class Structure extends DamageableCard {
	protected boolean underConstruction;
	
	protected Structure(String name, ResourceList cost, String text, String flavorText, int health) {
        super(name, cost, text, flavorText, health);
        underConstruction = true;
    }
	
	//TODO: copy method to "construct" a copy of this
	
    public boolean isUnderConstruction() {
		return underConstruction;
	}

	public void setUnderConstruction(boolean underConstruction) {
		this.underConstruction = underConstruction;
	}

}
