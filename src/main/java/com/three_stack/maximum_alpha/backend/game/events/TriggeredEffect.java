package com.three_stack.maximum_alpha.backend.game.events;

//@Todo: rename this since it's confusing
public class TriggeredEffect {
    protected Effect effect;
    protected Event event;

    public TriggeredEffect(Effect effect, Event event) {
        this.effect = effect;
        this.event = event;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
