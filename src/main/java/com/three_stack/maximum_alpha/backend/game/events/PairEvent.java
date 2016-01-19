package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.UUID;

public class PairEvent extends SingleCardEvent {
    protected transient Card target;

    public PairEvent(Card card, Card target, String description) {
        super(card, description);
        this.target = target;
    }

    public Card getTarget() {
        return target;
    }

    public void setTarget(Card target) {
        this.target = target;
    }

    @ExposeMethodResult("targetId")
    public UUID getTargetId() {
        return getTarget().getId();
    }
}
