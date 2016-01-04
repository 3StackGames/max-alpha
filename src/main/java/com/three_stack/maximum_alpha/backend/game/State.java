package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.google.gson.annotations.SerializedName;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.event.Event;
import com.three_stack.maximum_alpha.backend.game.utility.Serializer;
import com.three_stack.maximum_alpha.backend.server.Connection;

public class State {
	public enum Phase {
        START,
        MAIN,
        COMBAT,
        END
	}

    public enum TriggerPoint {
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
	public List<Event> eventHistory;
	public Phase currentPhase;
	/**
	 * Marks when combat has happened in Main Phase
	 * Default to false
	 */
	public boolean combatEnded;
	//corresponds to player indexes in the list
	public int turn;
    //two players each taking 1 turn is turnCount + 2
	public int turnCount;

    public Stack<Effect> effectStack;

	public List<Card> cardsPlayed;
	public transient Parameters parameters;
	
	//game over: winningPlayers, losingPlayers, tiedPlayers
	
	public State(Parameters parameters) {
		this.parameters = parameters;
		this.players = new ArrayList<>();
		this.eventHistory = new ArrayList<>();

		setupGame();
	}
	
	public void setupGame() {
        for(Connection connection : parameters.players) {
            Player player = new Player(connection, parameters.TOTAL_HEALTH);
            players.add(player);
            Deck deck = Deck.loadDeck(player.getConnection().deckId);
            deck.shuffle();
            player.setDeck(deck);
        }

		initialDraw();
        drawStart();
		//do other things here
	}
	
	public void initialDraw() {
		for (Player player : players) {
			for(int i = 0; i < parameters.INITIAL_DRAW_SIZE; i++) {
				player.draw();
			}
		}
	}
	
	//Phases
	
	public State nextPhase() {
		switch(currentPhase) {
		case START:
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
		currentPhase = Phase.START;
		
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
	
	public void processAction(Event event) {
	}
	
	public boolean isLegalAction(Action action) {
		return true;
	}

    private Player getTurnPlayer() {
        return players.get(turn);
    }

	@Override
	public String toString() {
		return Serializer.toJson(this);
	}
}
