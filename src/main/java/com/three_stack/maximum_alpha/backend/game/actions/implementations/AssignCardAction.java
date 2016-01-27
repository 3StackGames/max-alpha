package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

public class AssignCardAction extends ExistingCardAction {

    @Override
    public void run(State state) {
        Creature assignCreature = (Creature) player.getHand().takeCard(cardId, state.getTime(), state);
        state.createSingleCardEvent(assignCreature, "assign", state.getTime(), Trigger.ON_ASSIGN);
        player.getTown().add(assignCreature, state.getTime(), state);

        player.setHasAssignedOrPulled(true);
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
