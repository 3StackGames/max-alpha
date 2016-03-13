package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.cards.Tag;

public abstract class NonSpellCardZone<T extends NonSpellCard> extends Zone<T> {

    protected NonSpellCardZone(Player owner) {
        super(owner);
    }

    @Override
    public void add(T card, Time time, State state) {
        super.add(card, time, state);
        if(card.hasTag(Tag.TagType.LEGENDARY)) {
            card.propagateLegendaryLock(true, state);
        }
    }

    @Override
    public boolean remove(T card, Time time, State state) {
        boolean returnValue = super.remove(card, time, state);
        if(card.hasTag(Tag.TagType.LEGENDARY)) {
            card.propagateLegendaryLock(false, state);
        }
        return returnValue;
    }
}
