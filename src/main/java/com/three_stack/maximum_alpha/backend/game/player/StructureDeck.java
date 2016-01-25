package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.cards.CardFactory;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.Zone;
import com.three_stack.maximum_alpha.database_client.pojos.DBDeck;

import java.util.stream.Collectors;

public class StructureDeck extends Zone<Structure> {

    public StructureDeck(DBDeck deck, Player owner) {
        super(owner);
        cards = deck.getStructureCards().stream().map(CardFactory::create).map(card -> (Structure) card).collect(Collectors.toList());
    }

    @Override
    public Trigger getOnEnterTrigger() {
        return null;
    }

    @Override
    public Trigger getOnLeaveTrigger() {
        return null;
    }
}
