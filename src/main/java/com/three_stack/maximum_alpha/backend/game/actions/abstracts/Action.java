package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.player.Player;

/**
 * Always player Input
 */
public abstract class Action {
    protected UUID playerId;
    protected transient Player player;

    public abstract void run(State state);

    //TODO: instead create a custom serializer that converts ids to player?
    public void setup(State state) {
        player = state.findPlayer(playerId);
    }

    //TODO: throw Exception with error message if invalid?
    public abstract boolean isValid(State state);

    public Player getPlayer(State state) {
        for(Player player : state.getPlayingPlayers()) {
            if(playerId.equals(player.getPlayerId())) {
                return player;
            }
        }
        throw new IllegalArgumentException("Player Not Found");
    }

    public boolean isPlayerTurn(State state) {
        return (player == state.getTurnPlayer());
    }

    public boolean notInPrompt(State state) {
        return state.getCurrentPrompt() == null;
    }
}
