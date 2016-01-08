package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.events.SingleCardEvent;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardWithCostAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class PlayCardAction extends ExistingCardWithCostAction {

    @Override
    public void run(State state) {
        super.run(state);
        Player player = getPlayer(state);
        //find card in player's hand
        Card card = player.getHand().takeCard(cardId);
        if(card instanceof Creature) {
            player.pay(card.getCurrentCost());

            Event playEvent = new SingleCardEvent(card, player.getUsername() + " has played " + card.getName());
            state.addEvent(playEvent);


            Event enterFieldEvent = new SingleCardEvent(card, card.getName() + " entered the field");
            state.addEvent(enterFieldEvent);
            player.getField().add((Creature) card, state, enterFieldEvent);

        } else {
            //@Todo: Handle non-creature cards
        }
    }
}
