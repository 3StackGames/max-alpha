package com.three_stack.maximum_alpha.backend.game;

import com.three_stack.maximum_alpha.backend.game.cards.*;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.server.Connection;

import java.util.ArrayList;
import java.util.List;

public class Player implements Damageable {
    private String username;

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private transient Connection connection;
    private List<Card> hand;
    private Deck deck;
    private List<Creature> field;
    private List<Card> grave;
    private List<Worker> workers;
    private List<Structure> structures;
    private ResourceList resources;
    private int life;
    private int maxLife;

    public Player(Connection connection, int maxLife) {
        this.connection = connection;
        this.maxLife = maxLife;
        life = this.maxLife;

        resources = new ResourceList();
        hand = new ArrayList<>();
        field = new ArrayList<>();
        grave = new ArrayList<>();
        workers = new ArrayList<>();
        structures = new ArrayList<>();
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
        for(Worker worker : workers) {
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

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
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
