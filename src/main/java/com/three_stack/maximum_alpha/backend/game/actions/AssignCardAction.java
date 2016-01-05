package com.three_stack.maximum_alpha.backend.game.actions;

import com.three_stack.maximum_alpha.backend.game.Player;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class AssignCardAction extends ExistingCardAction {

    @Override
    public void run(State state) {
        Player player = state.getTurnPlayer();
        Card assignCard = player.getHand().takeCard(cardId);

        player.getWorkers().add(assignCard);
        player.setHasAssignedOrPulled(true);
        state.getEventHistory().add(new Event(player.getUsername() + " has assigned " + assignCard.getName() + " as a worker."));
    }
}
