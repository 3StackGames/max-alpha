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
    			player.setStatus(Status.TIE);
    		});
    	}
    	else {
    		deadPlayers.forEach((player) -> {
    			player.setStatus(Status.LOSE);
    		});
    		
	    	if(state.getPlayingPlayers().size() == 1) {
	    		state.getPlayingPlayers().get(0).setStatus(Status.WIN);
	    	}
    	}
	}
	
}
