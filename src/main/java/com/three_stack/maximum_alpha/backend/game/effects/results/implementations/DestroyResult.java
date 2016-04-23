package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.Step;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetStep;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

public class DestroyResult extends TargetResult {
	public DestroyResult(List<Step> steps) {
        super(steps);
    }

    public DestroyResult(DBResult dbResult) {
        super(dbResult);  
    }

    public DestroyResult(Result other) {
        super(other);
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value =  super.prepareNewValue();
        //do nothing for now
        return value;
    }

	@Override
	public void resolve(State state, Card source, Event event,
			Map<String, Object> value) {
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        source.kill(targets, state.getTime(), state);
	}
}
