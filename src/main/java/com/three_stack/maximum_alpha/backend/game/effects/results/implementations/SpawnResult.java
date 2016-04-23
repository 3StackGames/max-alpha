package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.three_stack.maximum_alpha.backend.game.effects.results.Step;
import org.bson.types.ObjectId;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.CardFactory;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetStep;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.utilities.DatabaseClientFactory;
import com.three_stack.maximum_alpha.database_client.pojos.DBCard;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

public class SpawnResult extends TargetResult {
    //@Todo: specify owner
    //@Todo: Add deck
    //@Todo: @Jason move values to value object for consistency
    enum SpawnZone {
        FIELD, COURTYARD, HAND
    }

    protected DBCard cardTemplate;
    protected int count;
    protected SpawnZone spawnZone;
    
    public SpawnResult(List<Step> steps, DBCard cardTemplate, int count, SpawnZone spawnZone) {
        super(steps);
        this.cardTemplate = cardTemplate;
        this.count = count;
        this.spawnZone = spawnZone;
    }

    public SpawnResult(DBResult dbResult) {
        super(dbResult);
        Map<String, Object> resultValue = dbResult.getValue();
        if(!resultValue.containsKey("card")) {
            throw new IllegalArgumentException("spawn result must specify card");
        }
        if(!resultValue.containsKey("count")) {
            throw new IllegalArgumentException("spawn result must specify count");
        }
        if(!resultValue.containsKey("zone")) {
            throw new IllegalArgumentException("spawn result must specify zone");
        }
        ObjectId cardId = (ObjectId) resultValue.get("card");
        cardTemplate = DatabaseClientFactory.getCard(cardId);
        count = (int) resultValue.get("count");
        spawnZone = SpawnZone.valueOf((String) resultValue.get("zone"));
    }

    public SpawnResult(Result other) {
        super(other);
        SpawnResult otherSpawnResult = (SpawnResult) other;
        this.cardTemplate = otherSpawnResult.cardTemplate;
        this.count = otherSpawnResult.count;
        this.spawnZone = otherSpawnResult.spawnZone;
    }

    @Override
    public void resolve(State state, Card source, Event event, Map<String, Object> value) {
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        List<Player> players = targets.stream().map(NonSpellCard::getController).collect(Collectors.toList());

        for(Player player : players) {
            List<Card> cards = IntStream.range(0, count)
                    .mapToObj(index -> CardFactory.create(cardTemplate))
                    .collect(Collectors.toList());
	        state.trackCardEffectsAndMarkController(cards, player);
	        Time spawnTime = state.getTime();
	        switch (spawnZone) {
	            case FIELD:
	                List<Creature> creatures = cards.stream()
	                        .map(card -> (Creature) card)
	                        .collect(Collectors.toList());
	                player.getField().addAll(creatures, spawnTime, state);
	                break;
	            case COURTYARD:
	                List<Structure> structures = cards.stream()
	                        .map(card -> (Structure) card)
	                        .collect(Collectors.toList());
	                player.getCourtyard().addAll(structures, spawnTime, state);
	                break;
	            case HAND:
	                player.getHand().addAll(cards, spawnTime, state);
	                break;
	            default:
	                throw new IllegalStateException("spawn zone not recognized");
	        }
        }
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value = super.prepareNewValue();
        //do nothing for now
        return value;
    }
}
