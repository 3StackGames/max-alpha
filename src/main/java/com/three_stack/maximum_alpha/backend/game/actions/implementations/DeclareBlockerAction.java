package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingPairAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.phases.BlockPhase;

import java.util.List;
import java.util.stream.Collectors;

public class DeclareBlockerAction extends ExistingPairAction {

    @Override
    public void run(State state) {
        Creature blocker = (Creature) card;
        Creature blockedTarget = (Creature) target;

        blocker.setBlockTarget(blockedTarget, state.getTime(), state);
    }

	@Override
	public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
		boolean correctPhase = state.isPhase(BlockPhase.class);
		boolean playerTurn = !isPlayerTurn(state);
		boolean isCreature = card instanceof Creature;
        if(!isCreature) {
            return false;
        }
		Creature blocker = (Creature) card;
		boolean isOnPlayerField = player.getField().getCards().contains(blocker);
		boolean canBlock = blocker.canBlock();
        boolean isTargetCreature = target instanceof Creature;
        if(!isTargetCreature) {
            return false;
        }
        List<Card> blockableTargets = state.getTurnPlayer().getField().getCards().stream()
                .filter(creature -> creature.isAttacking())
                .collect(Collectors.toList());
        boolean isValidBlockTarget = blockableTargets.contains(target);

		return notInPrompt && correctPhase && playerTurn && isOnPlayerField && canBlock && isValidBlockTarget;
	}
}
