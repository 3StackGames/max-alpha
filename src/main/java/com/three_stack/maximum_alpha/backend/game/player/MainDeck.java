package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.CardFactory;
import com.three_stack.maximum_alpha.backend.game.events.SingleCardEvent;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;
import com.three_stack.maximum_alpha.database_client.pojos.DBDeck;

import java.util.Collections;
import java.util.stream.Collectors;

public class MainDeck extends Zone<Card> {

    public MainDeck(DBDeck deck, Player owner) {
        super(owner);
        cards = deck.getMainCards().stream().map(CardFactory::create).collect(Collectors.toList());
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw(State state) {
        Card cardDrawn = remove(0);
        SingleCardEvent cardDrawEvent = new SingleCardEvent(owner, " has drawn " + cardDrawn.getName(), cardDrawn);
        state.addEvent(cardDrawEvent);
        state.notify(Trigger.ON_DRAW, cardDrawEvent);
        return cardDrawn;
    }
}
