package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardWithCostAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Spell;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class PlayCardAction extends ExistingCardWithCostAction {
    
    @Override
    public void run(State state) {
        //find a in player's hand
        Card card = player.getHand().takeCard(cardId, state.getTime(), state);

        player.pay(cost);

        Event playEvent = state.createSingleCardEvent(card, "play", state.getTime(), Trigger.ON_PLAY);

        if(card instanceof Creature) {
            player.getField().add((Creature) card, state.getTime(), state);
        } else if(card instanceof Spell){
            Spell spell = (Spell) card;
            spell.cast(playEvent, state);
        }
    }

	@Override
	public boolean isValid(State state) {
        if(!super.isValid(state)) {
            return false;
        }
		boolean notInPrompt = notInPrompt(state);
		boolean correctPhase = isPhase(state, MainPhase.class); //TODO: FubarPhase (instant phase)
		boolean playerTurn = isPlayerTurn(state);
		//boolean isPlayable = a.isPlayable(); //TODO: uncomment once implemented
        boolean inputCostSufficient = cost.hasResources(card.getCurrentCost());

		return notInPrompt && correctPhase && playerTurn && inputCostSufficient;
	}
}
