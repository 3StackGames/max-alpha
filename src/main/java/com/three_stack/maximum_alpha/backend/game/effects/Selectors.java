package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.Zone;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class Selectors {
    /**
     * Combinations
     */
    public static <T> List<T> structuresAndCastles(State state) {
        List<T> allStructuresAndCastles = structures(state);
        allStructuresAndCastles.addAll(castles(state));
        return allStructuresAndCastles;
    }
    /**
     * Creatures
     */
    public static <T> List<T> creatures(State state) {
        return state.getPlayers().stream()
                .map(Player::getField)
                .map(Zone::getCards)
                .flatMap(Collection::stream)
                .map( creature -> (T) creature)
                .collect(Collectors.toList());
    }

    public static <T> List<T> enemyCreatures(Player friendlyPlayer, State state) {
        return state.getPlayersExcept(friendlyPlayer).stream()
                .map(player -> player.getField().getCards())
                .flatMap(Collection::stream)
                .map(creature -> (T) creature)
                .collect(Collectors.toList());
    }
    /**
     * Structures
     */
    public static <T> List<T> structures(State state) {
        return state.getPlayingPlayers().stream()
                .map(player -> player.getCourtyard().getCards())
                .flatMap(Collection::stream)
                .map(card -> (T) card)
                .collect(Collectors.toList());
    }
    /**
     * Castles
     */
    public static <T> List<T> castles(State state) {
        return state.getPlayingPlayers().stream()
                .map(Player::getCastle)
                .map(castle -> (T) castle)
                .collect(Collectors.toList());
    }

    public static <T> List<T> enemyCastles(Player friendlyPlayer, State state) {
        return state.getPlayersExcept(friendlyPlayer).stream()
                .map(Player::getCastle)
                .map(castle -> (T) castle)
                .collect(Collectors.toList());
    }
}
