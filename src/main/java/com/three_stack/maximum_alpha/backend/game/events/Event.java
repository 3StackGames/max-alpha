package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.utilities.Serializer;

public class Event {

    /**
     * time occurred is essentially an id since no two events can occur at the same time.
     */
    protected int timeOccurred;
	protected final String description;
    protected Player player;
	
	public Event() {
		description = "";
	}

    public Event(String description) {
        this.description = description;
    }

    public Event(Player player, String action) {
        this.description = player.getUsername() + " has " + action;
        this.player = player;
    }

    public String getDescription() {
        return description;
    }

    public static Event joinEvents (Event a, Event b) {
        return new Event(a.getDescription() + " and " + b.getDescription());
    }

    public int getTimeOccurred() {
        return timeOccurred;
    }

    public void setTimeOccurred(int timeOccurred) {
        this.timeOccurred = timeOccurred;
    }

    @Override
    public String toString() {
        return Serializer.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return timeOccurred == event.timeOccurred;

    }

    @Override
    public int hashCode() {
        return timeOccurred;
    }
}
