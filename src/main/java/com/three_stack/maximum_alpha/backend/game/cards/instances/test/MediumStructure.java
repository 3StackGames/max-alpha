package com.three_stack.maximum_alpha.backend.game.cards.instances.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.ResourceList.Color;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;

public class MediumStructure extends Structure {

	public MediumStructure() {
		super("Medium Structure", new ResourceList(Color.WHITE, 1, 0), "TEST2", "Cooler than basic struct", 8);
	}

}
