package com.three_stack.maximum_alpha.backend.game.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.cards.CardFactory;
import com.three_stack.maximum_alpha.backend.game.utilities.MongoService;
import org.bson.Document;

public class Deck<T extends Card> extends Zone<T> {
	private Zone<Structure> buildables;

	public Deck() {
        super();
    }

    public Deck(List<T> cards) {
        super(cards);
    }
    
    public Deck(List<T> cards, List<Structure> buildables) {
    	super(cards);
    	this.buildables = new Zone<Structure>(buildables);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        return remove(0);
    }

    public List<T> drawCards(int numCards) {
        List<T> subDeck = subList(0, numCards);
        removeAll(subDeck);
        return subDeck;
    }

    public List<T> topCards(int numCards) {
        return subList(0, numCards);
    }

    public void placeAtBottom(T card) {
        if (containsAll(card)) {
            remove(card);
            add(card);
        }
    }

    public void placeAtBottom(Collection<T> bottomCards) {
        if (containsAll(bottomCards)) {
            removeAll(bottomCards);
            addAll(bottomCards);
        }
    }
	
    public Zone<Structure> getBuildables() {
		return buildables;
	}

	public void setBuildables(Zone<Structure> buildables) {
		this.buildables = buildables;
	}

    public static Deck<Card> loadDeck(int deckId) {
        MongoService mongoService = new MongoService();

        MongoCollection<Document> deckCollection = mongoService.getDeckCollection();
        MongoCollection<Document> cardCollection = mongoService.getCardCollection();

        MongoCursor<Document> deckCursor = deckCollection.find(new Document("id", deckId)).iterator();

        if(!deckCursor.hasNext()) {
            throw new IllegalArgumentException("Deck Not found");
        }

        Document deck = deckCursor.next();

        List<Integer> mainDeckIds = (List<Integer>) deck.get("mainDeckIds");
        List<Integer> structureIds = (List<Integer>) deck.get("buildableIds");

        List<Card> cards = new ArrayList<>();

        for(Integer mainDeckId : mainDeckIds) {
            Document cardDocument = cardCollection.find(new Document("id", mainDeckId)).first();

            Card card = CardFactory.create(cardDocument);
            cards.add(card);
        }
        
        List<Structure> buildables = new ArrayList<>();
        for(Integer structureId : structureIds) {
            Document cardDocument = cardCollection.find(new Document("id", structureId)).first();

            Structure structure = (Structure) CardFactory.create(cardDocument);
            buildables.add(structure);
        }

        return new Deck<>(cards, buildables);
    }
}
