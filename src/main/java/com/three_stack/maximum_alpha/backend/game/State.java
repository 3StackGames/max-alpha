package com.three_stack.maximum_alpha.backend.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.effects.*;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.events.SingleCardEvent;
import com.three_stack.maximum_alpha.backend.game.player.*;
import org.bson.types.ObjectId;

import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.player.StructureDeck;
import com.three_stack.maximum_alpha.backend.game.phases.Phase;
import com.three_stack.maximum_alpha.backend.game.phases.StartPhase;
import com.three_stack.maximum_alpha.backend.game.player.Player.Status;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.utilities.DatabaseClientFactory;
import com.three_stack.maximum_alpha.backend.game.utilities.Serializer;
import com.three_stack.maximum_alpha.backend.game.victories.VictoryHandler;
import com.three_stack.maximum_alpha.backend.server.Connection;
import com.three_stack.maximum_alpha.database_client.DatabaseClient;
import com.three_stack.maximum_alpha.database_client.pojos.DBDeck;

public class State {
    private final transient Parameters parameters;
    private List<Player> players;
    private List<Player> playingPlayers;
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
    private transient Queue<Prompt> promptQueue;
    private List<Card> cardsPlayed;
    private transient Map<UUID, Card> masterCardList;
    private transient Map<Trigger, List<Effect>> effects;

    /**
     * begins at 1, because initialization happens "at time 0"
     */
    private transient int timer = 1;
    private transient PriorityQueue<TriggeredEffect> triggeredEffects;
    private List<Player> winningPlayers;
    private List<Player> losingPlayers;
    private List<Player> tiedPlayers;
    private boolean gameOver;
    private transient VictoryHandler victoryHandler;

    public State(Parameters parameters) {
        this.parameters = parameters;
        this.players = new ArrayList<>();
        this.playingPlayers = new ArrayList<>();
        this.eventHistory = new ArrayList<>();
        this.promptQueue = new ArrayDeque<>();
        this.effects = new HashMap<>();
        this.winningPlayers = new ArrayList<>();
        this.losingPlayers = new ArrayList<>();
        this.tiedPlayers = new ArrayList<>();
        this.triggeredEffects = new PriorityQueue<>((Comparator<TriggeredEffect>) (a, b) -> {
            if (!a.getTrigerringEvent().getTime().equals(b.getTrigerringEvent().getTime())) {
                return a.getTrigerringEvent().getTime().getValue() - b.getTrigerringEvent().getTime().getValue();
            } else {
                return a.getEffect().getSource().getTimeEnteredZone().getValue() - b.getEffect().getSource().getTimeEnteredZone().getValue();
            }
        });
        setupGame();
    }

    public void setupGame() {
        victoryHandler = parameters.victoryHandler;
        gameOver = false;

        for (Connection connection : parameters.players) {
            //initialize player
            Player player = new Player(connection, parameters.TOTAL_HEALTH);
            players.add(player);
            //load decks
            DatabaseClient client = DatabaseClientFactory.create();
            ObjectId deckId = player.getConnection().deckId;
            DBDeck dbDeck = client.getDeckWithCards(deckId);
            MainDeck mainDeck = new MainDeck(dbDeck, player);
            StructureDeck structureDeck = new StructureDeck(dbDeck, player);
            trackCardEffectsAndMarkController(mainDeck.getCards(), structureDeck.getCards(), player);
            mainDeck.shuffle();
            player.setMainDeck(mainDeck);
            player.setStructureDeck(structureDeck);
        }
        playingPlayers.addAll(players);
        initialDraw();
        StartPhase.getInstance().start(this);
        //do other things here
        resolveTriggeredEffects();
    }

    private void trackCardEffectsAndMarkController(List<Card> mainCards, List<Structure> structureCards, Player controller) {
        for (Card card : mainCards) {
            card.setController(controller);
            trackCardEffects(card);
        }

        for (Structure structure : structureCards) {
            structure.setController(controller);
            trackCardEffects(structure);
        }
    }

    //@Todo: @Jason consider refactoring to streams - Jason
    private void trackCardEffects(Card card) {
        Map<Trigger, List<Effect>> effectsMap = card.getEffects();
        if (card.hasEffects() && !card.getEffects().isEmpty()) {
            Iterator<Map.Entry<Trigger, List<Effect>>> iterator = effectsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Trigger, List<Effect>> entry = iterator.next();

                for (Effect effect : entry.getValue()) {
                    effect.setSource(card);
                    addEffect(entry.getKey(), effect);
                }
            }
        }
    }

    private void initialDraw() {
        for (Player player : players) {
            for (int i = 0; i < parameters.INITIAL_DRAW_SIZE; i++) {
                player.draw(getTime(), this);
            }
        }
    }

    //Phase utilities

    public void completeStructures() {
        getTurnPlayer().completeStructures();
    }

    public void turnPlayerDraw() {
        if (turnCount > 0) {
            Player turnPlayer = getTurnPlayer();
            turnPlayer.draw(getTime(), this);
        }
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
        for (Player player : players) {
            if (player.getPlayerId().equals(id)) {
                return player;
            }
        }
        throw new IllegalArgumentException("Player Not Found");
    }

    public List<Player> getPlayersExcept(Player undesiredPlayer) {
        List<Player> otherPlayers = players.stream()
                .filter(player -> !player.equals(undesiredPlayer))
                .collect(Collectors.toList());

        return otherPlayers;
    }

    public synchronized boolean processAction(Action action) {
        if (isLegalAction(action)) {
            eventHistory.clear();
            action.run(this);
            resolveTriggeredEffects();
            return true;
        } else {
            return false; //TODO: return error message from isLegalAction -> action.isValid
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isLegalAction(Action action) {
        return action.isValid(this);
    }

    public Player getTurnPlayer() {
        return playingPlayers.get(turn);
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
        //Gets all target from each player, then collects them into the map
        masterCardList = playingPlayers.stream().map(Player::getAllCards).flatMap(p -> p.stream()).collect(Collectors.toMap(Card::getId, c -> c));

        return masterCardList;
    }

    public Map<UUID, Optional<Card>> generateVisibleCardList(Player player) {
        List<Card> visibleCardList = getPlayersExcept(player).stream().
                map(Player::getVisibleCards).
                flatMap(p -> p.stream()).
                collect(Collectors.toList());
        visibleCardList.addAll(player.getSelfVisibleCards());

        Map<UUID, Optional<Card>> visibleCardMap = new HashMap<>();
        masterCardList.entrySet().forEach((keyValue) -> {
            UUID key = keyValue.getKey();
            Card value = keyValue.getValue();
            if (visibleCardList.contains(value)) {
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

    public List<Player> getAllPlayers() {
        return players;
    }

    public List<Player> getPlayingPlayers() {
        return playingPlayers;
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

    public List<Effect> getEffects(Trigger trigger) {
        return effects.get(trigger);
    }

    public void addEffect(Trigger trigger, Effect effect) {
        if (effects.containsKey(trigger)) {
            effects.get(trigger).add(effect);
        } else {
            List<Effect> newEffects = new ArrayList<>();
            newEffects.add(effect);
            effects.put(trigger, newEffects);
        }
    }

    public boolean hasEffects(Trigger trigger) {
        return effects.containsKey(trigger);
    }

    public void addTriggeredEffect(TriggeredEffect triggeredEffect) {
        triggeredEffects.add(triggeredEffect);
    }

    public TriggeredEffect getTriggeredEffect() {
        return triggeredEffects.remove();
    }

    public boolean hasTriggeredEffect() {
        return !triggeredEffects.isEmpty();
    }

    public Time getTime() {
        return new Time(timer++);
    }

    /**
     * Call whenever an event is created to let the state know about the event.
     * The trigger parameter is optional and can be null.
     * @param event
     * @param trigger
     */
    public void addEvent(Event event, Trigger trigger) {
        eventHistory.add(event);
        if(trigger == null) return;

        List<Effect> effects = getEffects(trigger);
        if (effects == null) {
            return;
        }

        //add triggered effects to the queue of triggered effects
        effects.stream()
                .filter(effect -> effect.getChecks().parallelStream().allMatch(check -> check.run(this, effect, event)))
                .map(effect -> new TriggeredEffect(effect, event))
                .forEach(this::addTriggeredEffect);
    }

    public Event createSingleCardEvent(Card card, String type, Time time, Trigger trigger) {
        Event event = new SingleCardEvent(time, type, card);
        addEvent(event, trigger);
        return event;
    }

    /**
     * Traverses the "Effect Tree" and resolves them in BFS / Level-Order manner.
     */
    private void resolveTriggeredEffects() {
        while (hasTriggeredEffect()) {
            TriggeredEffect triggeredEffect = getTriggeredEffect();
            Effect currentEffect = triggeredEffect.getEffect();
            Event triggeringEvent = triggeredEffect.getTrigerringEvent();
            //index tracks which result / value pair we're on so we can pass the proper value to the run() method
            int[] index = {0};
            currentEffect.getResults().stream()
                    .forEachOrdered(result -> result.run(this, currentEffect.getSource(), triggeringEvent, currentEffect.getValues().get(index[0]++)));
            resolveDeaths();
        }
    }

    public void setPlayerStatus(Player player, Status status) {
        switch (status) {
            case LOSE:
                player.setStatus(Status.LOSE);
                playingPlayers.remove(player);
                losingPlayers.add(player);
                break;
            case TIE:
                player.setStatus(Status.TIE);
                playingPlayers.remove(player);
                tiedPlayers.add(player);
                break;
            case WIN:
                player.setStatus(Status.WIN);
                playingPlayers.remove(player);
                winningPlayers.add(player);
                break;
            default:
                break;
        }
    }

    public void resolveDeaths() {
        for (Player player : playingPlayers) {
            Field field = player.getField();
            Courtyard court = player.getCourtyard();
            Graveyard grave = player.getGraveyard();
            Time deathTime = getTime();
            List<Creature> deadCreatures = field.getCards().stream()
                    .filter(Creature::isDead).collect(Collectors.toList());
            deadCreatures.forEach(deadCreature -> {
            	deadCreature.reset();
                field.remove(deadCreature, getTime(), this);
                deadCreature.die(deathTime, this);
                grave.add(deadCreature, getTime(), this);
            });

            List<Structure> destroyedStructures = court.getCards().stream()
                    .filter(Structure::isDead).collect(Collectors.toList());
            destroyedStructures.forEach(destroyedStructure -> {
                court.remove(destroyedStructure, getTime(), this);
                destroyedStructure.die(deathTime, this);
            });
        }

        if (victoryHandler.determineVictory(this)) {
            gameOver();
        }
    }

    public void gameOver() {
        gameOver = true;
    }
}
