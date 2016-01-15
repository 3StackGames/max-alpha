package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.DamageableCard;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import org.apache.log4j.Logger;

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

        Stream<DamageableCard> castleStream = state.getPlayers().stream()
                .map(Player::getCastle);

        Stream<DamageableCard> structureStream = state.getPlayers().stream()
                .map(player -> player.getCourtyard().getCards())
                .flatMap(Collection::stream);

        Event damageEvent = new Event();

        Stream.concat(castleStream, structureStream)
                .forEach(damageableCard -> {
                    damageEvent.mergeEvent(damageableCard.takeDamage(damage, source));
                });

        state.addEvent(damageEvent);
    };

    public static Result DEAL_DAMAGE_ALL_CREATURES = (state, source, event, value) -> {
        int damage = (int) value;
        Event damageEvent = new Event();
        state.getPlayers().stream()
                .map(player -> player.getField().getCreatures())
                .flatMap(creatures -> creatures.stream())
                .forEach(creature -> {
                    damageEvent.mergeEvent(creature.takeDamage(damage, source));
                });
        state.addEvent(damageEvent);
    };

    public static Result DEAL_DAMAGE_ENEMY_CASTLES = (state, source, event, value) -> {
        Player controller = source.getController();
        int damage = (int) value;
        Event damageEvent = new Event();
        state.getPlayersExcept(controller).forEach(player -> {
            damageEvent.mergeEvent(player.takeDamage(damage, source));
        });
        state.addEvent(damageEvent);
    };

    public static Result DEAL_DAMAGE_RANDOM_ENEMY_CREATURE = (state, source, event, value) -> {
        Player controller = source.getController();
        int damage = (int) value;
        Random random = new Random();

        List<Creature> enemyCreatures = state.getPlayersExcept(controller).stream()
                .map(player -> player.getField().getCreatures())
                .flatMap(creatures -> creatures.stream())
                .collect(Collectors.toList());

        if (enemyCreatures.isEmpty()) {
            return;
        }

        Creature victim = enemyCreatures.get(random.nextInt(enemyCreatures.size()));
        victim.takeDamageSingleTarget(damage, source, state);
    };

    public static Result DEAL_DAMAGE_FRIENDLY_CASTLE = (state, source, event, value) -> {
        int damage = (int) value;
        source.getController().getCastle().takeDamageSingleTarget(damage, source, state);
    };
}
