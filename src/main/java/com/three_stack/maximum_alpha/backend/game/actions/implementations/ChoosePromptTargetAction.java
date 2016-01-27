package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.CardAction;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.steps.ChooseStep;

public class ChoosePromptTargetAction extends CardAction {

    @Override
    public void setup(State state) {
        super.setup(state);
        ChooseStep step = (ChooseStep) state.getCurrentPrompt().getCurrentStep();
        card = step.getChoices().stream()
                .filter(card -> card.getId().equals(cardId))
                .findFirst()
                .orElse(null);
    }

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
