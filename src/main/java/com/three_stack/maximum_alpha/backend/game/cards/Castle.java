package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;

public class Castle extends Structure {

	public Castle(int maxHealth) {
		super("Castle", new ResourceList(), "This is your base. If its health goes to 0, you lose!", "Nexus?", maxHealth);
	}

}
