package com.three_stack.maximum_alpha.backend.game.prompts;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.TargetStep;

/**
 * @Deprecated smooth combat
 */
public class AttackPrompt extends Prompt {

    public AttackPrompt(Card source, Player player, List<Card> targetables) {
        super(source, player);
        steps.add(new TargetStep("Select what you want attacked", targetables));
    }

    @Override
    public void processCurrentStep(Card input) {
        super.processCurrentStep(input);
        TargetStep step = (TargetStep) steps.get(0);
        step.complete(input);
    }

    @Override
    public void resolve(State state) {
        Creature attacker = (Creature) getSource();
        TargetStep targetStep = (TargetStep) steps.get(0);
        Card target = targetStep.getTarget();
        attacker.setAttackTarget((Structure) target);

        Event event = new Event(attacker.getName() + " is now attacking " + target.getName());
        state.addEvent(event);
    }

	@Override
	public boolean isValidInput(Card input) {
		TargetStep step = (TargetStep) getCurrentStep();
		return step.getTargetables().contains(input);	
	}
}
