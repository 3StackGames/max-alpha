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

public class ExhaustResult extends TargetResult {
	boolean exhaust;
	
	//If exhaust == true, exhausts the creature, otherwise unexhausts
	public ExhaustResult(List<TargetStep> targetSteps, boolean exhaust) {
        super(targetSteps);
        this.exhaust = exhaust;
    }

    public ExhaustResult(DBResult dbResult) {
        super(dbResult);  
        this.exhaust = (boolean) dbResult.getValue().get("exhaust");
    }

    public ExhaustResult(Result other) {
        super(other);
        ExhaustResult otherResult = (ExhaustResult) other;
        this.exhaust = otherResult.exhaust;
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value =  super.prepareNewValue();
        value.put("exhaust", exhaust);
        return value;
    }

	@Override
	public void resolve(State state, Card source, Event event,
			Map<String, Object> value) {
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        for(NonSpellCard target : targets) {
        	target.setExhausted(exhaust);
        }
	}
}
