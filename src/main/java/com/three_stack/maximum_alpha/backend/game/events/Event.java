package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.utilities.Serializer;

import java.util.ArrayList;
import java.util.List;

public class Event {

    /**
     * time occurred is essentially an id since no two events can occur at the same time.
     */
    protected int timeOccurred;
	protected List<String> descriptions;
    protected Player player;
	
	public Event() {
		setup();
	}

    public Event(String description) {
        setup();
        this.descriptions.add(description);
    }

    public Event(Player player, String action) {
        setup();
        this.descriptions.add(player.getUsername() + " has " + action);
        this.player = player;
    }

    protected void setup() {
        this.descriptions = new ArrayList<>();
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public void addDescription(String description) {
        this.getDescriptions().add(description);
    }

    public void mergeEvent(Event other) {
        getDescriptions().addAll(other.getDescriptions());
        this.timeOccurred = Math.min(timeOccurred, other.getTimeOccurred());
    }

    public static Event joinEvents (Event a, Event b) {
        Event event = new Event();
        event.getDescriptions().addAll(a.getDescriptions());
        event.getDescriptions().addAll(b.getDescriptions());
        int timeOccurred = Math.min(a.getTimeOccurred(), b.getTimeOccurred());
        event.setTimeOccurred(timeOccurred);
        return event;
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
