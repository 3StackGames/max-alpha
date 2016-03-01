package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

import java.util.Map;

/**
 * Does nothing. used when a result can't be parsed.
 */
public class EmptyResult extends Result {
    public EmptyResult(DBResult dbResult) {
        super(dbResult);
    }

    @Override
    public void resolve(State state, Card source, Event event, Map<String, Object> value) {

    }
}
