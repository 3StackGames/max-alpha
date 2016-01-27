package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.effects.events.Event;

//@Todo: rename this since it's confusing
public class TriggeredEffect {
    protected Effect effect;
    //trigerringEvent is what triggered the result
    protected Event trigerringEvent;

    public TriggeredEffect(Effect effect, Event trigerringEvent) {
        this.effect = effect;
        this.trigerringEvent = trigerringEvent;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public Event getTrigerringEvent() {
        return trigerringEvent;
    }

    public void setTrigerringEvent(Event trigerringEvent) {
        this.trigerringEvent = trigerringEvent;
    }
}
