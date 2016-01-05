package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class DeclareBlockerAction extends ExistingCardAction {

    @Override
    public void run(State state) {
    	if(blocker instanceof Creature) {
        	Creature creature = (Creature) blocker;
        	if(creature.canBlock()) {
        		creature.setBlockTarget(attacker);
        		eventHistory.add(new Event(creature.getId() + " is blocking " + attacker.getId()));
        	}    	
        }
    }
}
