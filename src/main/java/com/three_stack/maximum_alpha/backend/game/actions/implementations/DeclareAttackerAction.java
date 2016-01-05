package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class DeclareAttackerAction extends ExistingCardAction {
	
    @Override
    public void run(State state) {
    	Card attacker = state.findCard(cardId);
    	if(attacker instanceof Creature) {
        	Creature creature = (Creature) attacker;
        	Card target = state.findCard(targetId);
        	if(creature.canAttack()) {
        		creature.setAttackTarget(target);
        		Event event = new Event(creature.getId() + " is attacking " + target.getId());
        		state.addEvent(event);
        	}    	
        }
    }
}
