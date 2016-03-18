package com.three_stack.maximum_alpha.backend.game.victories;

import java.util.ArrayList;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.Player.Status;

public class DefaultVictoryHandler implements VictoryHandler {

	@Override
	public void determineVictory(State state) {  	
    	List<Player> players = state.getPlayingPlayers();
    	List<Player> deadPlayers = new ArrayList<>();

    	for(Player player : players) {
			if(player.getCastle().isDead()) {
				deadPlayers.add(player);
			}
    	}
    	
    	if(deadPlayers.size() == players.size()) {
    		players.forEach((player) -> {
    			state.setPlayerStatus(player, Status.TIE);
    		});
    	}
    	else {
    		deadPlayers.forEach((player) -> {
    			state.setPlayerStatus(player, Status.LOSE);
    		});
    		
	    	if(state.getPlayingPlayers().size() == 1) {
	    		state.setPlayerStatus(state.getPlayingPlayers().get(0), Status.WIN);
	    	}
    	}
	}
	
}
