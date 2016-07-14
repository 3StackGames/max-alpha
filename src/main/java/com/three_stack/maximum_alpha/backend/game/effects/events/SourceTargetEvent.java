package com.three_stack.maximum_alpha.backend.game.effects.events;

import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

import io.gsonfire.annotations.ExposeMethodResult;

public class SourceTargetEvent extends Event {
    protected final transient Card source;
    protected final transient Card target;

    public SourceTargetEvent(Time time, String type, Card source, Card target) {
        super(time, type);
        this.source = source;
        this.target = target;
    }

    public Card getSource() {
        return source;
    }

    public Card getTarget() {
        return target;
    }

    @ExposeMethodResult("targetId")
    public UUID getTargetId() {
        return target.getId();
    }

    @ExposeMethodResult("sourceId")
    public UUID getSourceId() {
        return source.getId();
    }
}
