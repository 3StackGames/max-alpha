package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Damageable;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.cards.Worker;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.server.Connection;

public class Player implements Damageable {
    private String username;

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private transient Connection connection;
    private long playerId;
    private List<Card> hand;
    private Deck deck;
    private List<Creature> field;
    private List<Card> grave;
    private List<Card> workers;
    private List<Structure> structures;
    private ResourceList resources;
    private int life;
    private int maxLife;

    public Player(Connection connection, int maxLife) {
        this.connection = connection;
        this.maxLife = maxLife;
        life = this.maxLife;
        playerId = connection.playerId;

        resources = new ResourceList();
        hand = new ArrayList<>();
        field = new ArrayList<>();
        grave = new ArrayList<>();
        workers = new ArrayList<>();
        structures = new ArrayList<>();
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
    	return cards;
    }

    public Connection getConnection() {
        return connection;
    }

    public void draw() {
        hand.add(deck.draw());
    }

    @Override
    public Event takeDamage(int damage, Card source) {
        life -= damage;
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

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public List<Creature> getField() {
        return field;
    }

    public void setField(List<Creature> field) {
        this.field = field;
    }

    public List<Card> getGrave() {
        return grave;
    }

    public void setGrave(List<Card> grave) {
        this.grave = grave;
    }

    public List<Card> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Card> workers) {
        this.workers = workers;
    }

    public List<Structure> getStructures() {
        return structures;
    }

    public void setStructures(List<Structure> structures) {
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
}
