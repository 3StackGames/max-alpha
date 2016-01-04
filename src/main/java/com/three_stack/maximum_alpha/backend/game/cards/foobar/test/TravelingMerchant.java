package com.three_stack.maximum_alpha.backend.game.cards.foobar.test;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

public class TravelingMerchant extends Creature {

    public TravelingMerchant() {
        super("TravelingMerchant", new ResourceList(3), "", "Selling baubles and doohickeys", 3, 5);
    }
}
