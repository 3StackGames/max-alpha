package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;

public class UndoAction extends Action {

	@Override
	public void run(State state) {
        Prompt prompt = state.getCurrentPrompt();
        if(prompt.undo()) {
        	state.removePrompt();
        }
	}

	@Override
	public boolean isValid(State state) {
		Prompt prompt = state.getCurrentPrompt();
		boolean hasPrompt = (prompt != null);
		boolean isCorrectPlayer = (player == prompt.getPlayer());
		boolean canUndo = prompt.canUndo();
		
		return hasPrompt && isCorrectPlayer && canUndo;
	}

}
