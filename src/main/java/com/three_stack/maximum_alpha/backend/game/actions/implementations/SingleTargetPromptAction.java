package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.prompts.SingleTargetPrompt;

public class SingleTargetPromptAction extends ExistingCardAction {
    @Override
    public void run(State state) {
        super.run(state);
        SingleTargetPrompt prompt = (SingleTargetPrompt) state.takePrompt();
        prompt.resolve(state, card);
    }
}
