package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.player.Player;

import java.util.UUID;

/**
 * Always player Input
 */
public abstract class Action {
    protected UUID playerId;

    protected transient Player player;

    public abstract void run(State state);

    //@Todo: instead create a custom serializer that converts ids to player
    public void setup(State state) {
        player = state.findPlayer(playerId);
    }

    public abstract boolean isValid(State state);

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

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
    
    public boolean isPhase(State state, Class phase) {
    	return state.getCurrentPhase().getClass().equals(phase);
    }
    
    public boolean notInPrompt(State state) {
    	return state.getCurrentPrompt() == null;
    }
}
