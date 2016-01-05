package com.three_stack.maximum_alpha.backend.game.actions;

import com.three_stack.maximum_alpha.backend.game.Player;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.Event;

import java.util.List;

public class AssignCardAction extends ExistingCardAction {

    @Override
    public void run(State state) {
        Player player = state.getTurnPlayer();
        List<Card> hand = player.getHand();
        Card assignCard = null;

        for(int i = 0; i < hand.size(); i++) {
            Card handCard = player.getHand().get(i);

            if(handCard.getId().equals(cardId)) {
                assignCard = handCard;
                hand.remove(i);
                break;
            }
        }

        if(assignCard == null) {
            throw new IllegalArgumentException("Card Not Found");
        }

        player.getWorkers().add(assignCard);
        state.getEventHistory().add(new Event(assignCard.getName() + " assigned as a worker."));
    }
}
