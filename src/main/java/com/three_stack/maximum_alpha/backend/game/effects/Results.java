package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.Zone;
import com.three_stack.maximum_alpha.backend.game.prompts.ChooseDamagePrompt;
import com.three_stack.maximum_alpha.backend.game.prompts.ChoosePrompt;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.prompts.SingleTargetDamagePrompt;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.TargetStep;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Results {
    static Logger log = Logger.getLogger(Results.class.getName());

    public static Result getResult(String resultName) {
        try {
            return (Result) Results.class.getField(resultName).get(Results.class.newInstance());
        } catch (Exception e) {
            log.warn("Tried getting result called " + resultName + " but failed. Falling back to DO_NOTHING");
            return DO_NOTHING;
        }
    }

    /**
     * Called when a result couldn't be properly parsed
     */
    public static Result DO_NOTHING = (state, source, event, value) -> {

    };

    public static Result DEAL_DAMAGE_ALL_STRUCTURES_AND_CASTLES = (state, source, event, value) -> {
        int damage = (int) value;

        Stream<NonSpellCard> castleStream = state.getPlayingPlayers().stream()
                .map(Player::getCastle);

        Stream<NonSpellCard> structureStream = state.getPlayingPlayers().stream()
                .map(player -> player.getCourtyard().getCards())
                .flatMap(Collection::stream);

        List<NonSpellCard> victims = Stream.concat(castleStream, structureStream).collect(Collectors.toList());
        source.dealDamage(victims, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_ALL_CREATURES = (state, source, event, value) -> {
        int damage = (int) value;
        List<NonSpellCard> victims = state.getPlayingPlayers().stream()
                .map(player -> player.getField().getCards())
                .flatMap(creatures -> creatures.stream())
                .collect(Collectors.toList());
        source.dealDamage(victims, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_ENEMY_CASTLES = (state, source, event, value) -> {
        Player controller = source.getController();
        int damage = (int) value;
        List<NonSpellCard> castles = state.getPlayersExcept(controller).stream()
                .map(Player::getCastle)
                .collect(Collectors.toList());
        source.dealDamage(castles, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_RANDOM_ENEMY_CREATURE = (state, source, event, value) -> {
        Player controller = source.getController();
        int damage = (int) value;
        Random random = new Random();

        List<Creature> enemyCreatures = state.getPlayersExcept(controller).stream()
                .map(player -> player.getField().getCards())
                .flatMap(creatures -> creatures.stream())
                .collect(Collectors.toList());

        if (enemyCreatures.isEmpty()) {
            return;
        }

        Creature victim = enemyCreatures.get(random.nextInt(enemyCreatures.size()));
        source.dealDamage(victim, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_FRIENDLY_CASTLE = (state, source, event, value) -> {
        int damage = (int) value;
        source.dealDamage(source.getController().getCastle(), damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_TARGET_CREATURE = (state, source, event, value) -> {
        int damage = (int) value;
        List<NonSpellCard> potentialTargets = state.getAllPlayers().stream()
                .map(Player::getField)
                .map(Zone::getCards)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if(potentialTargets.size() > 0) {
            SingleTargetDamagePrompt prompt = new SingleTargetDamagePrompt(source, source.getController(), true, damage, potentialTargets);
            state.addPrompt(prompt);
        }
    };

    public static Result CHOICE_DEAL_DAMAGE_CASTLE_OR_CREATURE = (state, source, event, value) -> {
        int damage = (int) value;
        List<Card> options = new ArrayList<>();
        options.add(new Creature("Deal Damage Castles", new ResourceList(0, 0, 0, 0, 0, 0, 0), "Deal " + damage + " damage to all castles", "", 0, 0, null));
        options.add(new Creature("Deal Damage Creatures", new ResourceList(0, 0, 0, 0, 0, 0, 0), "Deal " + damage + " damage to all creatures", "", 0, 0, null));
        ChoosePrompt prompt = new ChooseDamagePrompt(source, source.getController(), options, damage);
        state.addPrompt(prompt);
    };
}
