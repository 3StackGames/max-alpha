package com.three_stack.maximum_alpha.backend.game.victories;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Castle;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.Player.Status;

public class DefaultVictoryHandler implements VictoryHandler {

	@Override
	public boolean determineVictory(State state) {  	
    	List<Player> players = state.getPlayingPlayers();

    	boolean tie = true;
    	for(Player player : players) {
			Castle castle = player.getCastle();
			if(castle.isDead()) {
				state.setPlayerStatus(player, Status.LOSE);
			} else {
				tie = false;
			}
    	}
    	
    	if(tie) {
    		players.forEach((player) -> {
    			if(player.getStatus() == Status.PLAYING) {
        			state.setPlayerStatus(player, Status.TIE);
    			}
    		});
    		return true;
    	}
    	else {
	    	for(Player player : players) {
	    		if(player.getStatus() == Status.PLAYING) {
	    			boolean won = true;
		    		for(Player otherPlayer : state.getPlayersExcept(player)) {
		    			if(otherPlayer.getStatus() != Status.LOSE) {
		    				won = false;
		    				break;
		    			}
		    		}
		    		if(won) {
		    			state.setPlayerStatus(player, Status.WIN);
		    			return true;
		    		}
	    		}
	    	}
    	}
    	
    	return false;
	}
	
}
