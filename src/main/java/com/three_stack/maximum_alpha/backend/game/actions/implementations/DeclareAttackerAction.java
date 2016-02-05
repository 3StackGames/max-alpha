package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingPairAction;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.phases.AttackPhase;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

import java.util.List;
import java.util.stream.Collectors;

public class DeclareAttackerAction extends ExistingPairAction {
    @Override
    public void run(State state) {
        Creature attacker = (Creature) card;
        Structure targetStructure = (Structure) target;

        attacker.setAttackTarget(targetStructure, state.getTime(), state);
    }

	@Override
	public boolean isValid(State state) {
		boolean notInPrompt = notInPrompt(state);
		boolean correctPhase = isPhase(state, AttackPhase.class);
		boolean playerTurn = isPlayerTurn(state);
		boolean isCreature = card instanceof Creature;
        if(!isCreature) {
            return false;
        }
		Creature attacker = (Creature) card;
		boolean isOnPlayerField = player.getField().getCards().contains(attacker);
		boolean canAttack = attacker.canAttack();
        boolean isStructure = target instanceof Structure;
        if(!isStructure) {
            return false;
        }
        List<Card> attackableTargets = state.getPlayersExcept(player).stream()
                .map(Player::getTargets)
                .flatMap(p -> p.stream())
                .collect(Collectors.toList());
        boolean isValidTarget = attackableTargets.contains(target);

		return notInPrompt && correctPhase && playerTurn && isCreature && isOnPlayerField && canAttack && isValidTarget;
	}
}
