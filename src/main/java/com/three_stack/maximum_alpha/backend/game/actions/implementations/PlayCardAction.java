package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardWithCostAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class PlayCardAction extends ExistingCardWithCostAction {
    
    @Override
    public void run(State state) {
        Player player = getPlayer(state);
        //find a in player's hand
        Card card = player.getHand().takeCard(cardId, state.getTime(), state);

        player.pay(cost);

        state.createSingleCardEvent(card, "play", state.getTime(), Trigger.ON_PLAY);

        if(card instanceof Creature) {
            player.getField().add((Creature) card, state.getTime(), state);
        } else {
            //@Todo: Handle spells
        }
    }

	@Override
	public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
		boolean correctPhase = isPhase(state, MainPhase.class); //TODO: FubarPhase (instant phase)
		boolean playerTurn = isPlayerTurn(state);
		//boolean isPlayable = a.isPlayable(); //TODO: uncomment once implemented
        boolean inputCostSufficient = cost.hasResources(card.getCurrentCost());
		boolean playerHasInputCost = player.hasResources(cost); //technically covered by isPlayable in the future

		return notInPrompt && correctPhase && playerTurn && inputCostSufficient && playerHasInputCost;
	}
}
