package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardWithCostAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Spell;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;
import com.three_stack.maximum_alpha.backend.game.phases.PreparationPhase;
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
        } else if(card instanceof Spell) {
            Spell spell = (Spell) card;

            if(state.isPhase(MainPhase.class)) {
                spell.cast(playEvent, state);
            } else if(state.isPhase(PreparationPhase.class)) {
                spell.prepare(playEvent, state);
            }
        }
    }

	@Override
	public boolean isValid(State state) {
        if(!super.isValid(state)) {
            return false;
        }
		boolean notInPrompt = notInPrompt(state);
		boolean correctPhase = state.isPhase(MainPhase.class) || state.isPhase(PreparationPhase.class);
        boolean correctType = !state.isPhase(PreparationPhase.class) || card instanceof Spell;
		boolean playerTurn = isPlayerTurn(state);
		//boolean isPlayable = a.isPlayable(); //TODO: uncomment once implemented
        boolean inputCostSufficient = cost.hasResources(card.getCurrentCost());

		return notInPrompt && correctPhase && playerTurn && inputCostSufficient && correctType;
	}
}
