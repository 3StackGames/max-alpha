package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.ResourceList;

public class Castle extends Structure {

    public Castle(int maxHealth, Player controller) {
        super("Castle", new ResourceList(), "This is your base. If its health goes to 0, you lose!", "Nexus?", maxHealth);
        setController(controller);
    }

}
