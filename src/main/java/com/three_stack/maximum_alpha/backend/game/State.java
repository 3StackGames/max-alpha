package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.cards.Worker;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.events.Action;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.phases.*;
import com.three_stack.maximum_alpha.backend.game.utilities.Serializer;
import com.three_stack.maximum_alpha.backend.server.Connection;

public class State {
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

	private List<Player> players;
	private List<Event> eventHistory;
	private Phase currentPhase;
	/**
	 * Marks when combat has happened in Main Phase
	 * Default to false
	 */
	private boolean combatEnded;
	//corresponds to player indexes in the list
	private int turn;
    //two players each taking 1 turn is turnCount + 2
	private int turnCount;
    private Stack<Effect> effectStack;
	private List<Card> cardsPlayed;
	private final transient Parameters parameters;
	
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
        StartPhase.getInstance().start(this);
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
	public void endPhase() {
        currentPhase.end(this);
	}
	
	//Phase utilities

	public void turnPlayerDraw() {
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
		MainPhase.getInstance().end(this);
	}
    
    public Card findCard(long id) {
        return null;
    }
    
    public Player findPlayer(long id) {
        return players.get((int)id);
    }
    
    public void assign(long cardId, long pid) {
        Card card = findCard(cardId);
        if(card instanceof Worker) {
            Worker worker = (Worker) card;
            Player p = findPlayer(pid);
            eventHistory.add(worker.assign(this, p));
        }
    }
    
    public void playCard(long cardId, long pid) {
        Card card = findCard(cardId);
        if(card.isPlayable()) {
            Player p = findPlayer(pid);
            ResourceList cost = card.getCurrentCost();
            if(p.hasResources(cost)) {
                if(card instanceof Creature) {
                	if(p.playCreature((Creature) card)) {
                		eventHistory.add(new Event ("Player " + p.getUsername() + " played " + card.getId()));
                	}
                } else {
                    //spell stuff
                }
            }
            
        }
    }
    
    public void declareAttacker(long cardId, long pid, long combatTargetId) {
        Card card = findCard(cardId);
        if(card instanceof Creature) {
        	Creature creature = (Creature) card;
            Card target = findCard(combatTargetId);
        	if(creature.canAttack()) {
        		creature.setAttackTarget(target);
        		eventHistory.add(new Event(creature.getId() + " is attacking " + target.getId()));
        	}    	
        }
    }
    
    public void declareBlocker(long cardId, long pid, long combatTargetId) {
    	Card card = findCard(cardId);
        if(card instanceof Creature) {
        	Creature creature = (Creature) card;
            Card target = findCard(combatTargetId);
        	if(creature.canBlock()) {
        		creature.setBlockTarget(target);
        		eventHistory.add(new Event(creature.getId() + " is blocking " + target.getId()));
        	}    	
        }
    }
    
	//Major functions
	
	public void processAction(Action action) {
		eventHistory.clear();
		
		switch(action.getType()) {
		case CHANGE_PHASE: 
			endPhase();
			break;
		case ACTIVATE_EFFECT:
			break;
		case ASSIGN_CARD:
			assign(action.getActionCardId(), action.getPlayerId());
			break;
		case BUILD_STRUCTURE:
			break;
		case CHOOSE_EFFECT:
			break;
		case DECLARE_ATTACKER:
			declareAttacker(action.getActionCardId(), action.getPlayerId(), action.getCombatTargetId());
			break;
		case DECLARE_BLOCKER:
			declareBlocker(action.getActionCardId(), action.getPlayerId(), action.getCombatTargetId());
			break;
		case PLAY_CARD:
		    //if isValid(action)
			playCard(action.getActionCardId(), action.getPlayerId());
			break;
		case PULL_CARD:
			break;
		case SKIP_COMBAT:
			skipCombat();
			break;
		case TARGET_EFFECT:
			break;
		default:
			break;				
		}
	}
	
	public boolean isLegalAction(Action action) {
		return true;
	}

    public Player getTurnPlayer() {
        return players.get(turn);
    }

    public void refreshTurnPlayerCards() {
        Player player = getTurnPlayer();
        //look through field
        player.getField().forEach(Creature::attemptRefresh);
        //look through structures
        player.getStructures().forEach(Structure::attemptRefresh);
    }

	@Override
	public String toString() {
		return Serializer.toJson(this);
	}

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Event> getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(List<Event> eventHistory) {
        this.eventHistory = eventHistory;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public boolean isCombatEnded() {
        return combatEnded;
    }

    public void setCombatEnded(boolean combatEnded) {
        this.combatEnded = combatEnded;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public Stack<Effect> getEffectStack() {
        return effectStack;
    }

    public void setEffectStack(Stack<Effect> effectStack) {
        this.effectStack = effectStack;
    }

    public List<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(List<Card> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }
}
