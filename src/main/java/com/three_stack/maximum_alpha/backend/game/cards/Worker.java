package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.State;

public interface Worker {
	/**
	 * Called each turn at Draw Phase start when this a is assigned as a worker.
	 * 
	 * @param s The gamestate in which this a exists
	 * @return A resource list with resource values equal to the net change in resource quantities
	 */
	ResourceList work(State s);
	
	public boolean isAssignable();
	
	public boolean isPullable();
}
