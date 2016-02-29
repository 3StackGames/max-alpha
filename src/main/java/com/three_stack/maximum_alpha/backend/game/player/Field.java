package com.three_stack.maximum_alpha.backend.game.player;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.cards.Tag;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

import java.util.Collection;

public  class Field extends Zone<Creature> {
    public Field(Player owner) {
        super(owner);
    }

    @Override
    public Trigger getOnEnterTrigger() {
        return Trigger.ON_ENTER_FIELD;
    }

    @Override
    public Trigger getOnLeaveTrigger() {
        return Trigger.ON_LEAVE_FIELD;
    }

    @Override
    public void add(Creature card, Time time, State state) {
        super.add(card, time, state);
        if(card.hasTag(Tag.TagType.LEGENDARY)) {
            state.getPlayingPlayers().stream()
                    .map(Player::getAllCards)
                    .filter(playerCard -> playerCard instanceof NonSpellCard)
                    .map(playerCard -> (NonSpellCard) playerCard)
                    .filter(playerCard -> playerCard.hasTag(Tag.TagType.LEGENDARY))
                    .filter(playerCard -> playerCard.getName().equals(card.getName()))
                    .forEach(playerCard -> playerCard.setLegendaryLock(true));
        }
    }

    @Override
    public boolean remove(Creature card, Time time, State state) {
        boolean returnValue = super.remove(card, time, state);
        if(card.hasTag(Tag.TagType.LEGENDARY)) {
            state.getPlayingPlayers().stream()
                    .map(player -> player.getField().getCards())
                    .flatMap(Collection::stream)
                    .filter(fieldCard -> fieldCard.hasTag(Tag.TagType.LEGENDARY))
                    .filter(fieldCard -> fieldCard.getName().equals(card.getName()))
                    .forEach(fieldCard -> fieldCard.setLegendaryLock(false));
        }
        return returnValue;
    }
}
