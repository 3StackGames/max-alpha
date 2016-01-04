package com.three_stack.maximum_alpha.backend.game.event;

import com.three_stack.maximum_alpha.backend.game.utility.Serializer;

public class Event {
	protected final String description;
	
	public Event() {
		description = "";
	}

    public Event(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public static Event joinEvents (Event a, Event b) {
        return new Event(a.getDescription() + " and " + b.getDescription());
    }

    @Override
    public String toString() {
        return Serializer.toJson(this);
    }
}
