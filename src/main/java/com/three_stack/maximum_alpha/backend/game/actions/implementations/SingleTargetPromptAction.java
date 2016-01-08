package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;

public class SingleTargetPromptAction extends ExistingCardAction {
    @Override
    public void run(State state) {
        super.run(state);
        Prompt prompt = state.getCurrentPrompt();
        prompt.processCurrentStep(card);
        if(prompt.isDone()) {
            prompt.resolve(state);
            state.removePrompt();
        }
    }

	@Override
	public boolean isValid(State state) {
		Prompt prompt = state.getCurrentPrompt();
		boolean hasPrompt = (prompt != null);
		boolean isValidStepInput = prompt.isValidInput(card);
		boolean isCorrectPlayer = (player == prompt.getPlayer());
		
		return hasPrompt && isValidStepInput && isCorrectPlayer;
	}
}
