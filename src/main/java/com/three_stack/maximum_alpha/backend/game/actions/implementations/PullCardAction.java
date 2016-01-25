package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class PullCardAction extends ExistingCardAction {
    @Override
    public void run(State state) {
        Player player = state.getTurnPlayer();
        Card pullCard = player.getTown().takeCard(cardId, state.getTime(), state);

        player.getHand().add(pullCard, state.getTime(), state);
        player.setHasAssignedOrPulled(true);

        state.createSingleCardEvent(pullCard, "pull", state.getTime(), Trigger.ON_PULL);
    }

    @Override
    public boolean isValid(State state) {
        boolean notInPrompt = notInPrompt(state);
        boolean correctPhase = isPhase(state, "Main Phase"); //TODO: FubarPhase (instant phase)
        boolean playerTurn = isPlayerTurn(state);
        boolean playerCanPull = player.canAssignOrPull();
        Creature creature = player.getTown().findCard(cardId);
        boolean isPullable = creature.isPullable();

        return notInPrompt && correctPhase && playerTurn && playerCanPull && isPullable;
    }
}
