package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class BuildStructureAction extends ExistingCardAction {
    @Override
    public void run(State state) {
        Player player = getPlayer(state);
        //find card in player's hand
        Card card = player.getDeck().getBuildables().takeCard(cardId);
        if(card instanceof Structure) {
            player.pay(card.getCurrentCost());
            player.getCourtyard().add(new Structure((Structure)card));
            
            Event event = new Event(player.getUsername() + " is constructing " + card.getName());
            state.addEvent(event);
        } else {
            //ERROR
        }
    }

	@Override
	public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
		Structure structure = player.getDeck().getBuildables().findCard(cardId);
		boolean correctPhase = isPhase(state, "Main Phase");
		boolean playerTurn = isPlayerTurn(state);
		boolean isBuildable = (structure != null);
		boolean hasResources = player.hasResources(structure.getCurrentCost());
		
		return notInPrompt && correctPhase && playerTurn && isBuildable && hasResources;
	}
}
