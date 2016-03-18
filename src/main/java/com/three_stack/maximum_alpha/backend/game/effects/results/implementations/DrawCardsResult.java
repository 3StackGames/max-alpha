package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetStep;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

public class DrawCardsResult extends TargetResult {
	protected int count;

    public DrawCardsResult(List<TargetStep> targetSteps, int count) {
        super(targetSteps);
        this.count = count;
    }

    public DrawCardsResult(DBResult dbResult) {
        super(dbResult);
        this.count = (int) dbResult.getValue().get("count");    
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
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        int cardsToDraw = (int) value.get("count");
        for(NonSpellCard target : targets) {
        	for(int i = 0; i < cardsToDraw; i++) {
        		target.getController().draw(state.getTime(), state);
        	}
        }
    }
}
