package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.TargetStep;

import java.util.List;

public class AttackPrompt extends Prompt {

    public AttackPrompt(Card source, List<Card> targetables) {
        super(source);
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
}
