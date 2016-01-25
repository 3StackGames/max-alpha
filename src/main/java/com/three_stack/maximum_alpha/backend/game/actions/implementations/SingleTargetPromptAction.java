package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;

public class SingleTargetPromptAction extends ExistingCardAction {
    @Override
    public void run(State state) {
        Prompt prompt = state.getCurrentPrompt();
        prompt.completeCurrentStep(card);
        if(prompt.isDone()) {
            prompt.resolve(state);
            state.removePrompt();
        }
    }

	@Override
	public boolean isValid(State state) {
		Prompt prompt = state.getCurrentPrompt();
		boolean hasPrompt = (prompt != null);
        if(!hasPrompt) return false;
		boolean isValidStepInput = prompt.isValidInput(card);
		boolean isCorrectPlayer = (player == prompt.getPlayer());
		
		return isValidStepInput && isCorrectPlayer;
	}
}
