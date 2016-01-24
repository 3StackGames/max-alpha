package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.SingleCardOutcome;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;

public class AssignCardAction extends ExistingCardAction {

    @Override
    public void run(State state) {

        Creature assignCard = (Creature) player.getHand().takeCard(cardId, state);

        player.getTown().add(assignCard, state);
        player.setHasAssignedOrPulled(true);

        Event event = new SingleCardOutcome(player, " assigned " + assignCard.getName() + " as a worker.", assignCard);
        state.addEvent(event);
        state.notify(Trigger.ON_ASSIGN, event);
    }

    @Override
    public boolean isValid(State state) {
        boolean notInPrompt = notInPrompt(state);
        boolean correctPhase = isPhase(state, "Main Phase");
        boolean playerTurn = isPlayerTurn(state);
        boolean playerCanAssign = player.canAssignOrPull();
        Creature creature = (Creature) player.getHand().findCard(cardId);
        boolean isAssignable = creature.isAssignable();

        return notInPrompt && correctPhase && playerTurn && playerCanAssign && isAssignable;
    }
}
