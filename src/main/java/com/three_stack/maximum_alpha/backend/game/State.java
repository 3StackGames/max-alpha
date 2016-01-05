package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;
import com.three_stack.maximum_alpha.backend.game.phases.Phase;
import com.three_stack.maximum_alpha.backend.game.phases.StartPhase;
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
	//corresponds to player indexes in the list, starts at 0
	private int turn;
    //two players each taking 1 turn is turnCount + 2, starts at 0
	private int turnCount;
    private Queue<Effect> effectQueue;
	private List<Card> cardsPlayed;
	private final transient Parameters parameters;
	private Map<UUID, Card> cardList;
	
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
        players.forEach(Player::newTurn);
	}
    
    public Card findCard(UUID id) {
        return cardList.get(id);
    }
    
    public Player findPlayer(UUID id) {
        for(Player player : players) {
            if(player.getPlayerId().equals(id)) {
                return player;
            }
        }
        throw new IllegalArgumentException("Player Not Found");
    }

    public List<Player> getOtherPlayers(Player undesiredPlayer) {
        List<Player> otherPlayers = players.stream()
                .filter(player -> !player.equals(undesiredPlayer))
                .collect(Collectors.toList());

        /*
        English version of above

        for(Player player : players) {
            if(!player.equals(undesiredPlayer)) {
                otherPlayers.add(player);
            }
        }
        */

        return otherPlayers;
    }
    
    public void declareAttacker(Card attacker, long pid, Card target) {
        if(attacker instanceof Creature) {
        	Creature creature = (Creature) attacker;
        	if(creature.canAttack()) {
        		creature.setAttackTarget(target);
        		eventHistory.add(new Event(creature.getId() + " is attacking " + target.getId()));
        	}    	
        }
    }
    
    public void declareBlocker(Card blocker, long pid, Card attacker) {
        if(blocker instanceof Creature) {
        	Creature creature = (Creature) blocker;
        	if(creature.canBlock()) {
        		creature.setBlockTarget(attacker);
        		eventHistory.add(new Event(creature.getId() + " is blocking " + attacker.getId()));
        	}    	
        }
    }
	
	public void processAction(Action action) {
		eventHistory.clear();
		action.run(this);
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
    
    //For serialization
    
    public Map<UUID, Card> generateCardList() {
    	cardList = new HashMap<>();
    	for (Player player : players) {
    		Collection<Card> playerCards = player.getAllCards();
    		playerCards.forEach((card) -> {
    			cardList.put(card.getId(), card);
    		});
    	}
    	
    	return cardList;
    }

	@Override
	public String toString() {
    	generateCardList();
		return Serializer.toJson(this);
	}

	//Getters and setters
	
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

	public void addEvent(Event event) {
		eventHistory.add(event);
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
    
    public void addEffect(Effect effect) {
    	effectQueue.add(effect);
    }

    public Queue<Effect> getEffectQueue() {
        return effectQueue;
    }

    public void setEffectQueue(Queue<Effect> effectQueue) {
        this.effectQueue = effectQueue;
    }

    public List<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(List<Card> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }
}
