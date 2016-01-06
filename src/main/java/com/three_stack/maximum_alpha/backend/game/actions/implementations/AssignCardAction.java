package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.player.CardList;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class AssignCardAction extends ExistingCardAction {

    @Override
    public void run(State state) {
        super.run(state);
        CardList<Card> hand = player.getHand();
        Card assignCard = hand.takeCard(cardId);

        player.getWorkers().add(assignCard);
        player.setHasAssignedOrPulled(true);
        
        Event event = new Event(player.getUsername() + " has assigned " + assignCard.getName() + " as a worker.");
        state.addEvent(event);
    }
}
