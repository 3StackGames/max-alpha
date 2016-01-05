package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.Player;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardWithCostAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class PlayCardAction extends ExistingCardWithCostAction {

    @Override
    public void run(State state) {
        Player player = getPlayer(state);
        //find card in player's hand
        Card card = player.getHand().takeCard(cardId);
        if(card instanceof Creature) {
            player.pay(card.getCurrentCost());
            player.getField().add((Creature) card);
            
            Event event = new Event(player.getUsername() + " has played " + card.getName());
            state.addEvent(event);
        } else {
            //@Todo: Handle non-creature cards
        }
    }
}
