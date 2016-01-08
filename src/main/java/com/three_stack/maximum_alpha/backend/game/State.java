package com.three_stack.maximum_alpha.backend.game;

import java.util.*;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.player.Deck;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.events.Event;
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
    private Queue<Prompt> promptQueue;
	private List<Card> cardsPlayed;
	private final transient Parameters parameters;
	private transient Map<UUID, Card> masterCardList;
	private boolean gameOver;
	
	//game over: winningPlayers, losingPlayers, tiedPlayers
	
	public State(Parameters parameters) {
		this.parameters = parameters;
		this.players = new ArrayList<>();
		this.eventHistory = new ArrayList<>();
        this.promptQueue = new ArrayDeque<>();
		setupGame();
	}
	
	public void setupGame() {
        gameOver = false;
        
        for(Connection connection : parameters.players) {
            Player player = new Player(connection, parameters.TOTAL_HEALTH);
            players.add(player);
            Deck<Card> deck = Deck.loadDeck(player.getConnection().deckId);
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
	
	//Phase utilities

	public void completeStructures() {
		getTurnPlayer().completeStructures();
	}
	
	public void turnPlayerDraw() {
		if(turnCount > 0)
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
        return masterCardList.get(id);
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
	
	public void processAction(Action action) {
		eventHistory.clear();
		action.run(this);
	}
	
	public boolean isLegalAction(Action action) {
		return action.isValid(this);
	}

    public Player getTurnPlayer() {
        return players.get(turn);
    }

    public void refreshTurnPlayerCards() {
        Player player = getTurnPlayer();
        //look through field
        player.getField().getCards().forEach(Creature::attemptRefresh);
        //look through structures
        player.getCourtyard().getCards().forEach(Structure::attemptRefresh);
    }
    
    //For serialization
    
    public Map<UUID, Card> generateCardList() {
		//Gets all cards from each player, then collects them into the map
		masterCardList = players.stream().map(Player::getAllCards).flatMap(p -> p.stream()).collect(Collectors.toMap(Card::getId, c->c));
    	
    	return masterCardList;
    }
    
    public Map<UUID, Optional<Card>> generateVisibleCardList(Player player) {
    	List<Card> visibleCardList = getOtherPlayers(player).stream().
    										map(Player::getEnemyVisibleCards).
    											flatMap(p -> p.stream()).
    												collect(Collectors.toList());
    	visibleCardList.addAll(player.getSelfVisibleCards());
    	
    	Map<UUID, Optional<Card>> visibleCardMap = new HashMap<>();
        masterCardList.entrySet().forEach((keyValue) -> {
            UUID key = keyValue.getKey();
            Card value = keyValue.getValue();
            if(visibleCardList.contains(value)) {
                visibleCardMap.put(key, Optional.of(value));
            } else {
                visibleCardMap.put(key, Optional.empty());
            }
        });

        return visibleCardMap;
    }


	public String toString() {
    	generateCardList();
		return Serializer.toJsonCard(this);
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
    
    public void addPrompt(Prompt prompt) {
    	promptQueue.add(prompt);
    }

    public Prompt getCurrentPrompt() {
        return promptQueue.peek();
    }
    
    public void removePrompt() {
    	promptQueue.remove();
    }

    public List<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(List<Card> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

}
