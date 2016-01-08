package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Worker;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class AssignCardAction extends ExistingCardAction {

    @Override
    public void run(State state) {

        Creature assignCard = (Creature) player.getHand().takeCard(cardId);

        player.getTown().add(assignCard);
        player.setHasAssignedOrPulled(true);
        
        Event event = new Event(player.getUsername() + " has assigned " + assignCard.getName() + " as a worker.");
        state.addEvent(event);
    }

	@Override
	public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
		boolean correctPhase = isPhase(state, "Main Phase");
		boolean playerTurn = isPlayerTurn(state);
		boolean playerCanAssign = player.canAssignOrPull();
		Creature creature = (Creature) player.getHand().takeCard(cardId);
		boolean isAssignable = creature.isAssignable();
		
		return notInPrompt && correctPhase && playerTurn && playerCanAssign && isAssignable;
	}
}
