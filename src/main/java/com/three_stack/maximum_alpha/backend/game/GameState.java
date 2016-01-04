package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.server.Connection;

public class GameState {	
	public enum Phase {
		DRAW, MAIN, COMBAT, END	
	}

	public List<Player> players;
	public List<GameEvent> eventHistory;
	public Phase currentPhase;
	public boolean combatEnded = false;
	//corresponds to player indexes in the list
	public int turn = 0;
    //two players each taking 1 turn is turnCount + 2
	public int turnCount = 0;
	
	public List<Card> cardsPlayed;
	public GameParameters gp;
	
	//game over: winningPlayers, losingPlayers, tiedPlayers
	
	public GameState(GameParameters gp) {
		this.gp = gp;
		this.players = new ArrayList<>();
		
		setupGame();
	}
	
	public void setupGame() {
        for(Connection c : gp.players) {
            Player p = new Player(c, gp.TOTAL_HEALTH);
            players.add(p);
            Deck d = Game.loadDeck(p.getConnection().did);
            d.shuffle();
            p.setDeck(d);
        }

		initialDraw();

		//do other things here
	}
	
	public void initialDraw() {
		for (Player p : players) {
			for(int i = 0; i < gp.INITIAL_DRAW_SIZE; i++) {
				p.draw();
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
			draw();
		}
		
		gatherResources();
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
		drawStart();
	}
	
	//Phase utilities

	public void draw() {
		getTurnPlayer().draw();
	}
	
	public void gatherResources() {
		getTurnPlayer().gatherResources(this);
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

    private Player getTurnPlayer() {
        return players.get(turn);
    }
}
