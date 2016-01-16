package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.Parameters;
import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.*;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.server.Connection;

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

    //Zones
    private Hand hand;
    private MainDeck mainDeck;
    private StructureDeck structureDeck;
    private Field field;
    private Graveyard graveyard;
    private Town town;
    private Courtyard courtyard;

    private Castle castle;

    private ResourceList resources;
    private boolean hasAssignedOrPulled;
    private Status status;

	public enum Status {
    	WIN, LOSE, TIE, PLAYING
    }

    public Player(Connection connection, int baseMaxLife) {
        username = "Player " + usernameCounter++;

        this.connection = connection;
        playerId = UUID.randomUUID();

        hand = new Hand(this);
        field = new Field(this);

        graveyard = new Graveyard(this);
        town = new Town(this);
        courtyard = new Courtyard(this);

        resources = new ResourceList(Parameters.INITIAL_COLORLESS_MANA);
        castle = new Castle(baseMaxLife);
        
        status = Status.PLAYING;
    }
    
    public Collection<Card> getAllCards() {
    	Collection<Card> cards = new HashSet<>();
    	cards.addAll(mainDeck.getCards());
    	cards.addAll(hand.getCards());
    	cards.addAll(field.getCards());
    	cards.addAll(graveyard.getCards());
        cards.addAll(town.getCards());
        cards.addAll(courtyard.getCards());
        cards.addAll(structureDeck.getCards());
    	cards.add(castle);
    	
    	return cards;
    }
    
    public Collection<Card> getVisibleCards() {
    	Collection<Card> cards = new HashSet<>();
    	cards.addAll(field.getCards());
    	cards.addAll(graveyard.getCards());
        cards.addAll(town.getCards());
        cards.addAll(courtyard.getCards());
        cards.addAll(structureDeck.getCards());
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
        hand.add(mainDeck.draw(), state);
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

    public MainDeck getMainDeck() {
        return mainDeck;
    }

    public void setMainDeck(MainDeck mainDeck) {
        this.mainDeck = mainDeck;
    }

    public Field getField() {
        return field;
    }

    public Graveyard getGraveyard() {
        return graveyard;
    }

    public Town getTown() {
        return town;
    }

    public Courtyard getCourtyard() {
        return courtyard;
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

    public StructureDeck getStructureDeck() {
        return structureDeck;
    }

    public void setStructureDeck(StructureDeck structureDeck) {
        this.structureDeck = structureDeck;
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
   
    public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
