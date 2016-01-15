package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.Zone;
import com.three_stack.maximum_alpha.database_client.pojos.DBDeck;

import java.util.stream.Collectors;

public class StructureDeck extends Zone<Structure> {

    public StructureDeck(DBDeck deck, Player owner) {
        super(owner);
        cards = deck.getStructureCards().stream().map(CardFactory::create).map(card -> (Structure) card).collect(Collectors.toList());
    }
}
