package com.three_stack.maximum_alpha.backend.game.victories;

import java.util.ArrayList;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Castle;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.Player.Status;

public class DefaultVictoryHandler implements VictoryHandler {

	@Override
	public boolean determineVictory(State state) {  	
    	List<Player> players = state.getPlayingPlayers();
    	List<Player> losingPlayers = new ArrayList<>();
    	List<Player> tiedPlayers = new ArrayList<>();

    	boolean tie = true;
    	for(Player player : players) {
			Castle castle = player.getCastle();
			if(castle.isDead()) {
				losingPlayers.add(player);
			} else {
				tie = false;
			}
    	}
    	
    	for(Player loser : losingPlayers) {
			state.setPlayerStatus(loser, Status.LOSE);
    	}
    	
    	players = state.getPlayingPlayers();
    	if(tie) {
        	tiedPlayers.addAll(players);
    		tiedPlayers.forEach((player) -> {
    			state.setPlayerStatus(player, Status.TIE);
    		});
    		return true;
    	}
    	else {
	    	if(state.getPlayingPlayers().size() == 1) {
	    		state.setPlayerStatus(state.getPlayingPlayers().get(0), Status.WIN);
	    		return true;
	    	}
    	}
    	
    	return false;
	}
	
}
