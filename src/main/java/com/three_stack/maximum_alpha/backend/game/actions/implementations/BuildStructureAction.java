package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class BuildStructureAction extends ExistingCardAction {
    @Override
    public void run(State state) {
        Player player = getPlayer(state);
        Card card = state.findCard(cardId);
        player.pay(card.getCurrentCost());
        Structure structure = new Structure((Structure) card);
        state.trackCardEffectsAndMarkController(structure, player);
        player.getCourtyard().add(structure, state.getTime(), state);
    }

    @Override
    public boolean isValid(State state) {
        boolean notInPrompt = notInPrompt(state);
        boolean correctPhase = state.isPhase(MainPhase.class);
        boolean playerTurn = isPlayerTurn(state);
        Card structure = state.findCard(cardId);
        boolean isStructure = (structure != null && structure instanceof Structure);
        boolean isBuildable = player.getStructureDeck().getCards().contains(structure);
        boolean hasResources = player.hasResources(structure.getCurrentCost());

        return notInPrompt && correctPhase && playerTurn && isStructure && isBuildable && hasResources;
    }
}
