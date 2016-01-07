package com.three_stack.maximum_alpha.backend.game.player;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.*;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.cards.Worker;
import com.three_stack.maximum_alpha.backend.game.cards.instances.Castle;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.server.Connection;

public class Player {
    //@Todo: Actually retrieve their username
    private static int usernameCounter = 0;
    private String username;

	private transient Connection connection;
    private final UUID playerId;

    private Zone<Card> hand;
    private Deck<Card> deck;
    private Zone<Creature> field;
    private Zone<Card> grave;
    private Zone<Creature> town;
    private Zone<Structure> courtyard;

    private ResourceList resources;
    private Castle castle;
    private boolean hasAssignedOrPulled;

    public Player(Connection connection, int baseMaxLife) {
        username = "Player " + usernameCounter++;

        this.connection = connection;
        playerId = UUID.randomUUID();

        hand = new Zone<>();
        deck = new Deck<>();
        field = new Zone<>();
        grave = new Zone<>();
        town = new Zone<>();
        courtyard = new Zone<>();

        resources = new ResourceList(Parameters.INITIAL_COLORLESS_MANA);
        castle = new Castle(baseMaxLife);
    }
    
    public Collection<Card> getAllCards() {
    	Collection<Card> cards = new HashSet<>();
    	cards.addAll(deck.getCards());
    	cards.addAll(hand.getCards());
    	cards.addAll(field.getCards());
    	cards.addAll(grave.getCards());
        cards.addAll(town.getCards());
        cards.addAll(courtyard.getCards());
    	cards.add(castle);
    	
    	return cards;
    }
    
    public Collection<Card> getSelfVisibleCards() {
    	Collection<Card> cards = new HashSet<>();
    	cards.addAll(hand.getCards());
    	cards.addAll(field.getCards());
    	cards.addAll(grave.getCards());
        cards.addAll(town.getCards());
        cards.addAll(courtyard.getCards());
    	cards.add(castle);
    	
    	return cards;
    }
    
    public Collection<Card> getEnemyVisibleCards() {
    	Collection<Card> cards = new HashSet<>();
    	cards.addAll(field.getCards());
    	cards.addAll(grave.getCards());
        cards.addAll(town.getCards());
        cards.addAll(courtyard.getCards());
    	cards.add(castle);
    	
    	return cards;    	
    }
    
    public Collection<Card> getTargets() {
    	Collection<Card> targets = new HashSet<>();
    	targets.addAll(courtyard.getCards());
    	targets.add(castle);
    	return targets;
    }
    
    public void completeStructures() {
    	courtyard.getCards().stream().filter(Structure::isUnderConstruction).forEach((structure) -> {
    		structure.setUnderConstruction(false);
    	});
    }

    public void newTurn() {
    }

    public void draw() {
        hand.add(deck.draw());
    }

    public Event takeDamage(int damage, Card source) {
    	castle.takeDamage(damage, source);
        return new Event(username + " took " + damage + " damage from " + source.getName() + ".");
    }

    public void gatherResources(State state) {
        for(Card workerCard : town.getCards()) {
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

    /**
     * Generic Getters and Setters Below Here
     */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Zone<Card> getHand() {
        return hand;
    }

    public void setHand(Zone<Card> hand) {
        this.hand = hand;
    }

    public Deck<Card> getDeck() {
        return deck;
    }

    public void setDeck(Deck<Card> deck) {
        this.deck = deck;
    }

    public Zone<Creature> getField() {
        return field;
    }

    public void setField(Zone<Creature> field) {
        this.field = field;
    }

    public Zone<Card> getGrave() {
        return grave;
    }

    public void setGrave(Zone<Card> grave) {
        this.grave = grave;
    }

    public Zone<Creature> getTown() {
        return town;
    }

    public void setTown(Zone<Creature> town) {
        this.town = town;
    }

    public Zone<Structure> getCourtyard() {
        return courtyard;
    }

    public void setCourtyard(Zone<Structure> courtyard) {
        this.courtyard = courtyard;
    }

    public ResourceList getResources() {
        return resources;
    }

    public void setResources(ResourceList resources) {
        this.resources = resources;
    }
    
    public Castle getCastle() {
        return castle;
    }

    public void setCastle(Castle castle) {
        this.castle = castle;
    }

    public boolean hasAssignedOrPulled() {
        return hasAssignedOrPulled;
    }

    public void setHasAssignedOrPulled(boolean hasAssignedOrPulled) {
        this.hasAssignedOrPulled = hasAssignedOrPulled;
    }
}
