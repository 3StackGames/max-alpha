package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.Player;
import com.three_stack.maximum_alpha.backend.game.utilities.Serializer;

public class Event {
	protected final String description;
	
	public Event() {
		description = "";
	}

    public Event(String description) {
        this.description = description;
    }

    public Event(Player player, String action) {
        this.description = player.getUsername() + " has " + action;
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
