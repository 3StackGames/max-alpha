package com.three_stack.maximum_alpha.backend.game;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.cards.Worker;
import com.three_stack.maximum_alpha.backend.game.cards.instances.Base;
import com.three_stack.maximum_alpha.backend.game.cards.instances.test.PilotRecruit;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.server.Connection;

public class Player {
    private String username;

    //@Todo: Actually retrieve their username
    private static int usernameCounter = 0;

	private transient Connection connection;
    private final UUID playerId;
    private CardList<Card> hand;
    private Deck deck;
    private CardList<Creature> field;
    private CardList<Card> grave;
    private CardList<Card> workers;
    private CardList<Structure> structures;
    private ResourceList resources;
    private Base base;
    private int life;
    private int maxLife;
    private boolean hasAssignedOrPulled;

    public Player(Connection connection, int maxLife) {
        this.connection = connection;
        base = new Base(maxLife);
        playerId = UUID.randomUUID();
        username = "Player " + usernameCounter++;

        resources = new ResourceList(Parameters.INITIAL_COLORLESS_MANA);
        hand = new CardList<>();
        field = new CardList<>();

        //@Todo: REMOVE THIS
        Creature pilot = new PilotRecruit();
        pilot.setHasSummoningSickness(false);
        field.add(pilot);

        grave = new CardList<>();
        workers = new CardList<>();
        structures = new CardList<>();
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Base getBase() {
		return base;
	}
    
    public Collection<Card> getAllCards() {
    	Collection<Card> cards = new HashSet<>();
    	cards.addAll(deck.getDeck());
    	cards.addAll(hand);
    	cards.addAll(field);
    	cards.addAll(grave);
    	cards.addAll(structures);
    	workers.forEach((worker) -> {
    		if (worker instanceof Card) {
    			cards.add((Card)worker);
    		}
    	});
    	cards.add(base);
    	return cards;
    }
    
    public Collection<Card> getTargets() {
    	Collection<Card> targets = new HashSet<>();
    	targets.addAll(structures);
    	targets.add(base);
    	
    	return targets;
    }

    public Connection getConnection() {
        return connection;
    }

    public void newTurn() {
        setHasAssignedOrPulled(false);
    }

    public void draw() {
        hand.add(deck.draw());
    }

    public Event takeDamage(int damage, Card source) {
    	base.takeDamage(damage, source);
        return new Event(username + " took " + damage + " damage from " + source.getName() + ".");
    }

    public void gatherResources(State state) {
        for(Card workerCard : workers) {
            Worker worker = (Worker) workerCard;
            ResourceList resourceChange = worker.work(state);
            resources.add(resourceChange);
        }
    }
    
    public void pay(ResourceList cost) {
        resources.lose(cost);
    }
    
    public boolean hasResources(ResourceList other) {
        return resources.hasResources(other);
    }
    
    public boolean playCreature(Creature c) {
        if (hand.remove(c)) {
            pay(c.getCurrentCost());
            field.add(c);
            return true;
        }
        
        return false;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public CardList<Card> getHand() {
        return hand;
    }

    public void setHand(CardList<Card> hand) {
        this.hand = hand;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public CardList<Creature> getField() {
        return field;
    }

    public void setField(CardList<Creature> field) {
        this.field = field;
    }

    public CardList<Card> getGrave() {
        return grave;
    }

    public void setGrave(CardList<Card> grave) {
        this.grave = grave;
    }

    public CardList<Card> getWorkers() {
        return workers;
    }

    public void setWorkers(CardList<Card> workers) {
        this.workers = workers;
    }

    public CardList<Structure> getStructures() {
        return structures;
    }

    public void setStructures(CardList<Structure> structures) {
        this.structures = structures;
    }

    public ResourceList getResources() {
        return resources;
    }

    public void setResources(ResourceList resources) {
        this.resources = resources;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
    }

    public boolean isHasAssignedOrPulled() {
        return hasAssignedOrPulled;
    }

    public void setHasAssignedOrPulled(boolean hasAssignedOrPulled) {
        this.hasAssignedOrPulled = hasAssignedOrPulled;
    }
}
