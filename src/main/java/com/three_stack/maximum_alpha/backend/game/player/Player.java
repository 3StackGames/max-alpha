package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.Parameters;
import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.*;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.server.Connection;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class Player {
    //@Todo: Actually retrieve their username
    private static int usernameCounter = 0;

    private String username;

	private transient Connection connection;
    private final UUID playerId;

    private Hand hand;
    private Deck deck;
    private Field field;
    private Zone<Card> grave;
    private Zone<Creature> town;
    private Zone<Structure> courtyard;
    private Castle castle;

    private ResourceList resources;
    private boolean hasAssignedOrPulled;

    public Player(Connection connection, int baseMaxLife) {
        username = "Player " + usernameCounter++;

        this.connection = connection;
        playerId = UUID.randomUUID();

        hand = new Hand(this);
        deck = new Deck();
        field = new Field();

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
    	cards.addAll(field.getCreatures());
    	cards.addAll(grave.getCards());
        cards.addAll(town.getCards());
        cards.addAll(courtyard.getCards());
        cards.addAll(deck.getBuildables().getCards());
    	cards.add(castle);
    	
    	return cards;
    }
    
    public Collection<Card> getVisibleCards() {
    	Collection<Card> cards = new HashSet<>();
    	cards.addAll(field.getCreatures());
    	cards.addAll(grave.getCards());
        cards.addAll(town.getCards());
        cards.addAll(courtyard.getCards());
        cards.addAll(deck.getBuildables().getCards());
    	cards.add(castle);
        
        return cards;	
    }
    
    public Collection<Card> getSelfVisibleCards() {
    	Collection<Card> cards = getVisibleCards();
    	cards.addAll(hand.getCards());
    	
    	return cards;
    }
    
    public Collection<Card> getTargets() {
    	Collection<Card> targets = courtyard.getCards().stream().filter(structure -> !structure.isUnderConstruction()).collect(Collectors.toSet());
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

    public void draw(State state) {
        hand.add(deck.draw(), state);
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

    public Hand getHand() {
        return hand;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
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

    //TODO: add support for effects which give extra assigns/pulls? or should those be separate effects
    public boolean canAssignOrPull() {
        return !hasAssignedOrPulled;
    }

    public void setHasAssignedOrPulled(boolean hasAssignedOrPulled) {
        this.hasAssignedOrPulled = hasAssignedOrPulled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return playerId != null ? playerId.equals(player.playerId) : player.playerId == null;

    }

    @Override
    public int hashCode() {
        return playerId != null ? playerId.hashCode() : 0;
    }
}
