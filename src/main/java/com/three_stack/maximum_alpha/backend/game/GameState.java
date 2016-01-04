package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.server.Connection;

public class GameState {	
	public enum Phase {
		DRAW, MAIN, COMBAT, END	
	}

    public enum Breakpoint {
        ON_EVENT,
        ON_RESOURCE_GENERATE,
        ON_RESOURCE_SPEND,
        ON_CARD_ENTER_HAND,
            ON_DRAW,
            ON_PULL,
        ON_CARD_LEAVE_HAND,
            ON_PLAY_CARD,
            ON_DISCARD_CARD,
            ON_ASSIGN,
        ON_COMBAT,
            ON_ATTACK,
            ON_BLOCK,
        ON_DAMAGE,
        ON_TARGET,
        ON_BUILD_STRUCTURE,
        ON_STRUCTURE_COMPLETE,
        ON_CARD_ENTER_FIELD,
        ON_CARD_LEAVE_FIELD,
            ON_DEATH,
        ON_ENTER_GRAVEYARD,
        ON_LEAVE_GRAVEYARD,
        ON_REFRESH,
        ON_EXHAUST,

        ON_BEGIN_PHASE_START,
        ON_BEGIN_PHASE_END,
        ON_MAIN_PHASE_START,
        ON_MAIN_PHASE_END,
        ON_ATTACK_PHASE_START,
        ON_ATTACK_PHASE_END,
        ON_BLOCK_PHASE_START,
        ON_BLOCK_PHASE_END,
        ON_END_PHASE_START,
        ON_END_PHASE_END
    }

	public List<Player> players;
	public List<GameEvent> eventHistory;
	public Phase currentPhase;
	public boolean combatEnded;
	//corresponds to player indexes in the list
	public int turn;
    //two players each taking 1 turn is turnCount + 2
	public int turnCount;

    public Stack<Effect> effectStack;

	public List<Card> cardsPlayed;
	public GameParameters gameParameters;
	
	//game over: winningPlayers, losingPlayers, tiedPlayers
	
	public GameState(GameParameters gameParameters) {
		this.gameParameters = gameParameters;
		this.players = new ArrayList<>();
		
		setupGame();
	}
	
	public void setupGame() {
        for(Connection connection : gameParameters.players) {
            Player player = new Player(connection, gameParameters.TOTAL_HEALTH);
            players.add(player);
            Deck deck = Game.loadDeck(player.getConnection().deckId);
            deck.shuffle();
            player.setDeck(deck);
        }

		initialDraw();

		//do other things here
	}
	
	public void initialDraw() {
		for (Player player : players) {
			for(int i = 0; i < gameParameters.INITIAL_DRAW_SIZE; i++) {
				player.draw();
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
		for(Card card : cardsPlayed) {
			GameEvent event = card.effect(this, e);
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
