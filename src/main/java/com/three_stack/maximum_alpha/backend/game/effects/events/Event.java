package com.three_stack.maximum_alpha.backend.game.effects.events;

import com.three_stack.maximum_alpha.backend.game.Time;

public abstract class Event {
    protected final String type;
    protected final Time time;

    public Event(Time time, String type) {
        this.time = time;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Time getTime() {
        return time;
    }
}
