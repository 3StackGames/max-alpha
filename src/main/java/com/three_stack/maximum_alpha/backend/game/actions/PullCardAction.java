package com.three_stack.maximum_alpha.backend.game.actions;

import com.three_stack.maximum_alpha.backend.game.Player;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class PullCardAction extends ExistingCardAction {
    @Override
    public void run(State state) {
        Player player = state.getTurnPlayer();
        Card pullCard = player.getWorkers().takeCard(cardId);

        player.getHand().add(pullCard);
        player.setHasAssignedOrPulled(true);
        
        Event event = new Event(player.getUsername() + " has pulled " + pullCard.getName() + " from work");
        state.addEvent(event);
    }
}
