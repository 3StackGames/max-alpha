package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.PlayerResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.PlayerStep;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.utilities.ValueExpression;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

public class DrawCardsResult extends PlayerResult {
	  protected ValueExpression count;

    public DrawCardsResult(List<PlayerStep> playerSteps, ValueExpression count) {
        super(playerSteps);
        this.count = count;
    }

    public DrawCardsResult(DBResult dbResult) {
        super(dbResult);
        this.count = new ValueExpression(dbResult.getValue().get("count"));    
    }

    public DrawCardsResult(Result other) {
        super(other);
        DrawCardsResult otherDrawResult = (DrawCardsResult) other;
        this.count = otherDrawResult.count;
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value =  super.prepareNewValue();
        value.put("count", count);
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resolve(State state, Card source, Event event, Map<String, Object> value) {
        List<Player> players = (List<Player>) value.get("players");
        int cardsToDraw = ((ValueExpression) value.get("count")).eval(state);
        for(Player player : players) {
        	for(int i = 0; i < cardsToDraw; i++) {
        		player.draw(state.getTime(), state);
        	}
        }
    }
}
