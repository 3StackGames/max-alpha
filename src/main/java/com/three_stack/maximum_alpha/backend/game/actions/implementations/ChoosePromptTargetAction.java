package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.CardAction;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.ChoosePrompt;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.steps.ChoosePromptStep;

public class ChoosePromptTargetAction extends CardAction {

    @Override
    public void setup(State state) {
        super.setup(state);
        ChoosePrompt choosePrompt = (ChoosePrompt) state.getCurrentPrompt();
        card = choosePrompt.getOptions().stream()
                .filter(option -> option.getId().equals(cardId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void run(State state) {
        ChoosePrompt choosePrompt = (ChoosePrompt) state.getCurrentPrompt();
        choosePrompt.setChoice(card);
        choosePrompt.resolve(state);
        state.removePrompt();
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
