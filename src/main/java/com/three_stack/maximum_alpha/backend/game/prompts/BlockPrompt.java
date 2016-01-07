package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.TargetStep;

import java.util.List;

public class BlockPrompt extends Prompt {

    public BlockPrompt(Card source, List<Card> targetables) {
        super(source);
        steps.add(new TargetStep("Select attacker to block", targetables));
    }

    @Override
    public void processCurrentStep(Card input) {
        super.processCurrentStep(input);
        TargetStep step = (TargetStep) steps.get(0);
        step.complete(input);
    }

    @Override
    public void resolve(State state) {
        Creature blocker = (Creature) getSource();
        TargetStep targetStep = (TargetStep) steps.get(0);
        Creature target = (Creature) targetStep.getTarget();

        blocker.setBlockTarget(target);
        target.addBlocker(blocker);

        Event event = new Event(blocker.getName() + " is now blocking " + target.getName());
        state.addEvent(event);
    }
}
