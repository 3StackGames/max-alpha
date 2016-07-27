package com.three_stack.maximum_alpha.backend.game.attributes;

import java.util.List;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Castle;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.events.SourceDamageTargetEvent;

public class DamageTakenAttribute extends Attribute {
	
	public DamageTakenAttribute() {
		super(Trigger.ON_DAMAGE);
	}

	@Override
	public void addEvent(Trigger t, Event e, State s) {
		super.addEvent(t, e, s);
		if (!(e instanceof SourceDamageTargetEvent) || t != trigger) {
			return;
		}
		
		events.add(e);
	}
	
	@Override
	public int process(Event e) {
		SourceDamageTargetEvent de = (SourceDamageTargetEvent) e;
		return de.getDamage();
	}

	@Override
	protected List<Event> filterEvents(List<Filter> filters, Card source) {
		List<Event> turnEvents = super.filterEvents(filters, source);
		
		if (filters.contains(Filter.CASTLE)) {
			turnEvents = turnEvents.stream().map(event -> (SourceDamageTargetEvent) event).
					filter(event -> event.getTarget() instanceof Castle).
					collect(Collectors.toList());
		}
		
		//TODO: account for controller switching
		if (filters.contains(Filter.SELF)) {
			return turnEvents.stream().map(event -> (SourceDamageTargetEvent) event).
					filter(event -> event.getTarget().getController().equals(source.getController())).
					collect(Collectors.toList());
		}
		else if (filters.contains(Filter.ENEMY)) {
			return turnEvents.stream().map(event -> (SourceDamageTargetEvent) event).
					filter(event -> !event.getTarget().getController().equals(source.getController())).
					collect(Collectors.toList());
		}
		
		return turnEvents;
	}

}
