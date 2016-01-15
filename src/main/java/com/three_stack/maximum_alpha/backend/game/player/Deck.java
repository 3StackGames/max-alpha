package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.CardFactory;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.database_client.pojos.DBDeck;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Deck extends Zone<Card> {
    //@Todo: remove this for production
    public static ObjectId DEFAULT_ID = new ObjectId("568ec4b9bbdcf16c2c000003");

	private Zone<Structure> buildables;

	public Deck() {
        super();
    }

    public Deck(List<Card> cards) {
        super(cards);
    }
    
    public Deck(List<Card> cards, List<Structure> buildables) {
    	super(cards);
    	this.buildables = new Zone<Structure>(buildables);
    }

    public Deck(DBDeck deck) {
        cards = deck.getMainCards().stream().map(CardFactory::create).collect(Collectors.toList());
        buildables = new Zone<>();
        buildables.cards = deck.getStructureCards().stream().map(CardFactory::create).map(card -> (Structure) card).collect(Collectors.toList());
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        return remove(0);
    }

    public List<Card> drawCards(int numCards) {
        List<Card> subDeck = subList(0, numCards);
        removeAll(subDeck);
        return subDeck;
    }

    public List<Card> topCards(int numCards) {
        return subList(0, numCards);
    }

    public void placeAtBottom(Card card, State state) {
        if (contains(card)) {
            remove(card);
            add(card, state);
        }
    }
	
    public Zone<Structure> getBuildables() {
		return buildables;
	}

	public void setBuildables(Zone<Structure> buildables) {
		this.buildables = buildables;
	}
}
