package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Result {
    protected List<Step> preparationSteps;

    public Result(DBResult dbResult) {
        preparationSteps = new ArrayList<>();
    }

    public abstract void resolve(State state, Card source, Event event, Map<String, Object> value);

    /**
     * adds necessary attributes to the value for a new result
     */
    public Map<String, Object> prepareNewValue() {
        return new HashMap<>();
    }

    public List<Step> getPreparationSteps() {
        return preparationSteps;
    }
}
