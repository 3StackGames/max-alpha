package com.three_stack.maximum_alpha.backend.game.cards.instances;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;

public class Base extends Structure {

	public Base(int maxHealth) {
		super("Base", new ResourceList(), "This is your base. If its health goes to 0, you lose!", "Nexus?", maxHealth);
	}

}
