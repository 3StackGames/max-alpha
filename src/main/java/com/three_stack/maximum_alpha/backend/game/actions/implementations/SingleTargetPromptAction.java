package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.TargetStep;

public class SingleTargetPromptAction extends ExistingCardAction {
    @Override
    public void run(State state) {
        super.run(state);
        Prompt prompt = state.takePrompt();
        prompt.processStep(card);
        if(!prompt.hasNext()) {
            prompt.resolve(state);
        } else {

        }
    }
}
