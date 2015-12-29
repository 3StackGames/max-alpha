package com.three_stack.maximum_alpha.backend.game;

import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.server.Player;

public class GameState {	
	public enum Phase {
		DRAW, MAIN, COMBAT, END	
	}
	
	public List<Player> players;
	public List<GameEvent> eventHistory;
	public Phase currentPhase;
	public boolean combatEnded = false;
	public int turn = 0;
	public int turnCount = 0;
	
	public Map<Player, List<Card>> hands;
	public Map<Player, List<Card>> fields;
	public Map<Player, List<Card>> graves;
	public Map<Player, List<Card>> workers;
	public Map<Player, List<Card>> structures;
	//mana
	
	public List<Card> cardsPlayed;
	
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
	
	public void newTurn() {
		combatEnded = false;
		turnCount++;
		turn++;
		if (turn >= players.size()) {
			turn = 0;
		}		
	}
	
	public void skipCombat() {
		combatEnded = true;
		mainEnd();
	}
	
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
		return false;
	}
}
