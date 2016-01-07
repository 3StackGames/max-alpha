package com.three_stack.maximum_alpha.backend.game.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.three_stack.maximum_alpha.backend.Config;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.cards.CardFactory;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.BasicStructure;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.BlackCreature;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.BlueCreature;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.GreenCreature;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.MediumStructure;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.MilitiaMinuteman;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.RedCreature;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.WhiteCreature;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.YellowCreature;
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
        MongoClient client = new MongoClient((String) Config.getProperty("mongo.address"), Integer.parseInt(Config.getProperty("mongo.port")));
        MongoDatabase database = client.getDatabase("MaxAlpha");

        MongoCollection<Document> cardCollection = database.getCollection("cards");

        MongoCursor<Document> cursor = cardCollection.find().iterator();

        if(!cursor.hasNext()) {
            throw new IllegalStateException("Unable to retrieve cards from database");
        }

        Document cardDocument = cursor.next();

        List<Card> cards = new ArrayList<>();

        for(int i = 0; i < 50; i++) {
            Card card = CardFactory.create(cardDocument);
            cards.add(card);
        }
        
        List<Structure> buildables = new ArrayList<>();     
        buildables.add(new BasicStructure());
        buildables.add(new MediumStructure());

        return new Deck<>(cards, buildables);
    }
}
