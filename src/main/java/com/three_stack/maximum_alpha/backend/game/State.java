package com.three_stack.maximum_alpha.backend.game;

import java.util.*;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.actions.abstracts.Action;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.cards.StructureDeck;
import com.three_stack.maximum_alpha.backend.game.events.Effect;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;
import com.three_stack.maximum_alpha.backend.game.events.TriggeredEffect;
import com.three_stack.maximum_alpha.backend.game.player.MainDeck;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.phases.Phase;
import com.three_stack.maximum_alpha.backend.game.phases.StartPhase;
import com.three_stack.maximum_alpha.backend.game.utilities.DatabaseClientFactory;
import com.three_stack.maximum_alpha.backend.game.utilities.Serializer;
import com.three_stack.maximum_alpha.backend.server.Connection;
import com.three_stack.maximum_alpha.database_client.DatabaseClient;
import com.three_stack.maximum_alpha.database_client.pojos.DBDeck;
import org.bson.types.ObjectId;

public class State {
    private final transient Parameters parameters;
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
    private transient Map<UUID, Card> masterCardList;
    private boolean gameOver;
    private transient Map<Trigger, List<Effect>> effects;

    /**
     * begins at 1, because initialization happens "at time 0"
     */
    private int timer = 1;
    //@Todo: make this a priority queue with 2-factors: low effectCount (main priority) and low ageInZone (secondary priority)
    private transient PriorityQueue<TriggeredEffect> triggeredEffects;
    //game over: winningPlayers, losingPlayers, tiedPlayers

    public State(Parameters parameters) {
        this.parameters = parameters;
        this.players = new ArrayList<>();
        this.eventHistory = new ArrayList<>();
        this.promptQueue = new ArrayDeque<>();
        this.effects = new HashMap<>();
        this.triggeredEffects = new PriorityQueue<>((Comparator<TriggeredEffect>) (a, b) -> {
            if (!a.getEvent().equals(b.getEvent())) {
                return a.getEvent().getTimeOccurred() - b.getEvent().getTimeOccurred();
            } else {
                return a.getEffect().getSource().getTimeEnteredZone() - b.getEffect().getSource().getTimeEnteredZone();
            }
        });
        setupGame();
    }

    private void setupGame() {

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
                player.draw(this);
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
            turnPlayer.draw(this);
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

    public void processAction(Action action) {
        eventHistory.clear();
        action.run(this);
        resolveTriggeredEffects();
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
        masterCardList = players.stream().map(Player::getAllCards).flatMap(p -> p.stream()).collect(Collectors.toMap(Card::getId, c -> c));

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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addEvent(Event event) {
        event.setTimeOccurred(timer++);
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

    public int getTime() {
        return timer++;
    }

    public void notify(Trigger trigger, Event event) {
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

    /**
     * Traverses the "Effect Tree" and resolves them in BFS / Level-Order manner.
     */
    private void resolveTriggeredEffects() {
        while (hasTriggeredEffect()) {
            TriggeredEffect triggeredEffect = getTriggeredEffect();
            Effect currentEffect = triggeredEffect.getEffect();
            Event effectEvent = triggeredEffect.getEvent();
            //index tracks which result / value pair we're on so we can pass the proper value to the run() method
            int[] index = {0};
            currentEffect.getResults().stream()
                    .forEachOrdered(result -> result.run(this, currentEffect.getSource(), effectEvent, currentEffect.getValues().get(index[0]++)));
        }
    }
}
