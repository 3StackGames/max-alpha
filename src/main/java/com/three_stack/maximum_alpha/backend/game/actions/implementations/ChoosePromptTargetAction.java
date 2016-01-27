package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.ChooseStep;

import java.util.UUID;

public class ChoosePromptTargetAction extends Action {
    protected UUID cardId;
    protected Card card;

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
