package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;

/**
 * @Deprecated: Not implemented after Refactor
 * TODO: implement
 */
public class SkipStepAction extends Action {

    @Override
    public void run(State state) {
        /*
		Prompt prompt = state.getCurrentPrompt();
		prompt.skipStep();
		if(prompt.isDone())
			state.removePrompt();
			*/
    }

    @Override
    public boolean isValid(State state) {
        return false;
//        Prompt prompt = state.getCurrentPrompt();
//        boolean hasPrompt = (prompt != null);
//        boolean isCorrectPlayer = (player == prompt.getPlayer());
//        boolean canSkip = prompt.canSkip();
//
//        return hasPrompt && isCorrectPlayer && canSkip;
    }

}
