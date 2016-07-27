package com.three_stack.maximum_alpha.backend.game.attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;

public abstract class Attribute {
	protected final List<Event> events;
	protected final Trigger trigger;
	protected Time lastTurnStart;
	protected Time lastTurnEnd;
	
	public Attribute(Trigger t) {
		events = new ArrayList<>();
		trigger = t;
		lastTurnStart = Time.getSetup();
		lastTurnEnd = Time.getSetup();
	}
	
	public void addEvent(Trigger t, Event e, State s) {
		if (t == Trigger.ON_END_PHASE_END) {
			lastTurnStart = lastTurnEnd;
			lastTurnEnd = e.getTime();
		}
	}
	
	public abstract int process(Event e);
	
	protected List<Event> filterEvents(List<Filter> filters, Card source) {
		if (filters.contains(Filter.LAST_TURN)) {
			return events.stream().
					filter(event -> event.getTime().compareTo(lastTurnEnd) <= 0 && event.getTime().compareTo(lastTurnStart) > 0).
					collect(Collectors.toList());			
		}
		else if (filters.contains(Filter.TURN)) {
			return events.stream().
					filter(event -> event.getTime().compareTo(lastTurnEnd) > 0).
					collect(Collectors.toList());
		}
		
		return events;
	}
	
	protected int sumValues(List<Event> events) {
		return events.stream().map(event -> process(event)).mapToInt(i -> i.intValue()).sum();
	}
	
	public int getValue(List<Filter> filters, Card source) {
		return sumValues(filterEvents(filters, source));
	}
	
	public enum Filter {
		LAST_TURN,
		TURN,
		ENEMY,
		SELF,
		CASTLE
	}
}

