package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Spell;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.ChooseResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

import java.util.Map;

public class ChooseCardResult extends ChooseResult {
    public ChooseCardResult(DBResult dbResult) {
        super(dbResult);
    }

    public ChooseCardResult(Result other) {
        super(other);
    }

    @Override
    public void resolve(State state, Card source, Event event, Map<String, Object> value) {
        //create a copy of the card for the user to play
        Card card = (Card) value.get("card");

        Event playEvent = state.createSingleCardEvent(card, "play", state.getTime(), null);

        if(card instanceof Creature) {
            source.getController().getField().add((Creature) card, state.getTime(), state);
        } else if(card instanceof Spell) {
            Spell spell = (Spell) card;
            spell.cast(playEvent, state);
        }
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        return super.prepareNewValue();
    }
}
