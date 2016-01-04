package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.server.Player;

public class GameState {	
	public enum Phase {
		DRAW, MAIN, COMBAT, END	
	}
	
	final int INITIAL_DRAW_SIZE = 7;
	final int TOTAL_HEALTH = 40;
	
	public List<Player> players;
	public List<GameEvent> eventHistory;
	public Phase currentPhase;
	public boolean combatEnded = false;
	public int turn = 0;
	public int turnCount = 0;
	public int playerCount;
	public Player turnPlayer;
	
	public Map<Player, List<Card>> hands;
	public Map<Player, Deck> decks;
	public Map<Player, List<Card>> fields;
	public Map<Player, List<Card>> graves;
	public Map<Player, List<Worker>> workers;
	public Map<Player, List<Card>> structures;
	public Map<Player, ResourceList> resources;
	public Map<Player, Integer> lifes;
	
	public List<Card> cardsPlayed;
	public GameParameters gp;
	
	//game over: winningPlayers, losingPlayers, tiedPlayers
	
	public GameState(GameParameters gp) {
		this.gp = gp;
		this.players = gp.players;
		playerCount = players.size();
		
		setupGame();
	}
	
	public void setupGame() {
		resources = new HashMap<Player, ResourceList>(playerCount);
		for (Player p : players) {
			Deck d = Game.loadDeck(p.did);
			d.shuffle();
			decks.put(p, d);
			initialDraw();
			
			fields.put(p, new ArrayList<Card>());
			workers.put(p, new ArrayList<Worker>());
			structures.put(p, new ArrayList<Card>());
			graves.put(p, new ArrayList<Card>());
			resources.put(p, new ResourceList());
			lifes.put(p, TOTAL_HEALTH);
		}
		
		turnPlayer = players.get(turn);
		//do other things here
	}
	
	public void initialDraw() {
		for (Player p : players) {
			for(int i = 0; i < INITIAL_DRAW_SIZE; i++) {
				draw(p); //consider refactoring to p.draw
			}
		}
	}
	
	//Phases
	
	public GameState nextPhase() {
		switch(currentPhase) {
		case DRAW:
			drawEnd();
			break;
		case MAIN:
			mainEnd();
			break;
		case COMBAT:
			combatEnd();
			break;
		case END:
			endEnd();
			break;
		}
		
		return this;
	}
	
	public void drawStart() {
		currentPhase = Phase.DRAW;
		
		if(turnCount > 0) {
			draw(turnPlayer);
		}
		
		gatherResources();
		buildStructures();
	}
	
	public void drawEnd() {
		mainStart();
	}
	
	public void mainStart() {
		currentPhase = Phase.MAIN;
	}
	
	public void mainEnd() {
		if(combatEnded) {
			endStart();
		}
		else {
			combatStart();
		}
	}
	
	public void combatStart() {
		currentPhase = Phase.COMBAT;
	}
	
	public void combatEnd() {
		combatEnded = true;	
	}
	
	public void endStart() {
		currentPhase = Phase.END;	
	}
	
	public void endEnd() {
		newTurn();	
	}
	
	//Phase utilities
	
	public void gatherResources() {
		gatherResources(turnPlayer);
	}
	
	public void gatherResources(Player p) {
		ResourceList rlist = resources.get(p);
		List<Worker> wlist = workers.get(p);
		for(Worker worker : wlist) {
			ResourceList resourceChange = worker.work(this);
			rlist.add(resourceChange);
		}
	}
	
	public void buildStructures() {
		buildStructures(turnPlayer);
	}
	
	public void buildStructures(Player p) {
		
	}
	
	public void draw() {
		draw(turnPlayer);
	}
	
	public void draw(Player p) {
		List<Card> hand = hands.get(p);
		Deck deck = decks.get(p);
		hand.add(deck.draw());
	}
	
	public void newTurn() {
		combatEnded = false;
		turnCount++;
		turn++;
		if (turn >= players.size()) {
			turn = 0;
		}		
		turnPlayer = players.get(turn);
	}
	
	public void skipCombat() {
		combatEnded = true;
		mainEnd();
	}
	
	//Major functions
	
	public void runTriggers(GameEvent e) {
		for(Card c : cardsPlayed) {
			GameEvent event = c.effect(this, e);
			if(event != null) {
				//apply effect here
			}
		}
	}
	
	public void processAction(GameEvent event) {
	}
	
	public boolean isLegalAction(GameAction action) {
		return true;
	}
}
