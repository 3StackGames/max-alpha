package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;

import java.util.List;
import java.util.UUID;

public class ChooseAction extends Action {
    protected List<UUID> choices;

    @Override
    public void run(State state) {
    }

	@Override
	public boolean isValid(State state) {
		// TODO Auto-generated method stub
		return false;
	}
}
