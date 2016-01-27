package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class PullCardAction extends ExistingCardAction {
    @Override
    public void run(State state) {
        Creature pullCreature = player.getTown().takeCard(cardId, state.getTime(), state);
        state.createSingleCardEvent(pullCreature, "pull", state.getTime(), Trigger.ON_PULL);
        player.getHand().add(pullCreature, state.getTime(), state);

        player.setHasAssignedOrPulled(true);
    }

    @Override
    public boolean isValid(State state) {
        boolean notInPrompt = notInPrompt(state);
        boolean correctPhase = isPhase(state, MainPhase.class); //TODO: FubarPhase (instant phase)
        boolean playerTurn = isPlayerTurn(state);
        boolean playerCanPull = player.canAssignOrPull();
        Creature creature = player.getTown().findCard(cardId);
        boolean isPullable = creature.isPullable();

        return notInPrompt && correctPhase && playerTurn && playerCanPull && isPullable;
    }
}
