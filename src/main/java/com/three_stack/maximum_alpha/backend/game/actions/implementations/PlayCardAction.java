package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.events.SingleCardEvent;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardWithCostAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class PlayCardAction extends ExistingCardWithCostAction {
    
    @Override
    public void run(State state) {
        Player player = getPlayer(state);
        //find card in player's hand
        Card card = player.getHand().takeCard(cardId, state);
        if(card instanceof Creature) {
            player.pay(card.getCurrentCost());

            Event playEvent = new SingleCardEvent(card, player.getUsername() + " has played " + card.getName());
            state.addEvent(playEvent);

            player.getField().add((Creature) card, state);

        } else {
            //@Todo: Handle spells
        }
    }

	@Override
	public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
		boolean correctPhase = isPhase(state, "Main Phase"); //TODO: FubarPhase (instant phase)
		boolean playerTurn = isPlayerTurn(state);
		//boolean isPlayable = card.isPlayable(); //TODO: uncomment once implemented
		boolean hasResources = player.hasResources(card.getCurrentCost()); //technically covered by isPlayable in the future
		
		return notInPrompt && correctPhase && playerTurn && hasResources;
	}
}
