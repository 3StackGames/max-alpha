package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.player.ResourceList;

public abstract class ExistingCardWithCostAction extends ExistingCardAction {

    protected ResourceList cost;

    @Override
    public boolean isValid(State state) {
        boolean playerHasInputCost = getPlayer(state).hasResources(cost); //technically covered by isPlayable in the future
        return playerHasInputCost;
    }

    /**
     * Auto-Generated Getters and Setters Below
     */
    public ResourceList getCost() {
        return cost;
    }

    public void setCost(ResourceList cost) {
        this.cost = cost;
    }
}
