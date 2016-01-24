package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.CardFactory;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.database_client.pojos.DBDeck;

import java.util.Collections;
import java.util.stream.Collectors;

public class MainDeck extends Zone<Card> {

    public MainDeck(DBDeck deck, Player owner) {
        super(owner);
        cards = deck.getMainCards().stream().map(CardFactory::create).collect(Collectors.toList());
    }

    @Override
    public Trigger getOnEnterTrigger() {
        return null;
    }

    @Override
    public Trigger getOnLeaveTrigger() {
        return null;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw(Time time, State state) {
        Card cardDrawn = removeWithoutEvent(0);
        state.createSingleCardEvent(cardDrawn, "draw", time, Trigger.ON_DRAW);
        return cardDrawn;
    }
}
