package com.three_stack.maximum_alpha.backend.game.effects.events;

import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import io.gsonfire.annotations.ExposeMethodResult;
import java.util.UUID;

public class SourceTargetEvent extends Event {
    protected transient Card source;
    protected transient Card target;

    public SourceTargetEvent(Time time, String type, Card source, Card target) {
        super(time, type);
        this.source = source;
        this.target = target;
    }

    public Card getSource() {
        return source;
    }

    public void setSource(Card source) {
        this.source = source;
    }

    public Card getTarget() {
        return target;
    }

    public void setTarget(Card target) {
        this.target = target;
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
