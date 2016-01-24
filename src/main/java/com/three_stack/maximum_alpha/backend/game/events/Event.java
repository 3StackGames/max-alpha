package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.events.outcomes.Outcome;
import com.three_stack.maximum_alpha.backend.game.utilities.Serializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Event {

    /**
     * time occurred is essentially an id since no two events can occur at the same time.
     *
     * It gets set when it's added to the state.
     */
    protected int timeOccurred;
	protected List<Outcome> outcomes;

	public Event() {
        setup();
	}

    public Event(Outcome outcome) {
        setup();
        outcomes.add(outcome);
    }

    protected void setup() {
        outcomes = new ArrayList<>();
    }

    public int getTimeOccurred() {
        return timeOccurred;
    }

    public void setTimeOccurred(int timeOccurred) {
        this.timeOccurred = timeOccurred;
    }

    public void addOutcome(Outcome outcome) {
        outcomes.add(outcome);
    }

    public void addAllOutcomes(Collection<Outcome> outcomes) {
        this.outcomes.addAll(outcomes);
    }

    public List<Outcome> getOutcomes() {
        return outcomes;
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
