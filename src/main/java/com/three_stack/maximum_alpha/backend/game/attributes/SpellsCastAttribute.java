package com.three_stack.maximum_alpha.backend.game.attributes;

import java.util.List;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.events.SingleCardEvent;

public class SpellsCastAttribute extends Attribute {
	
	public SpellsCastAttribute() {
		super(Trigger.ON_PLAY);
	}

	@Override
	public void addEvent(Trigger t, Event e, State s) {
		super.addEvent(t, e, s);
		if (!(e instanceof SingleCardEvent) || t != trigger || !"playSpell".equals(e.getType())) {
			return;
		}
		
		events.add(e);
	}
	
	@Override
	public int process(Event e) {
		return 1;
	}

	@Override
	protected List<Event> filterEvents(List<Filter> filters, Card source) {
		List<Event> turnEvents = super.filterEvents(filters, source);

		//TODO: account for controller switching
		if (filters.contains(Filter.SELF)) {
			return turnEvents.stream().map(event -> (SingleCardEvent) event).
					filter(event -> event.getCard().getController().equals(source.getController())).
					collect(Collectors.toList());
		}
		else if (filters.contains(Filter.ENEMY)) {
			return turnEvents.stream().map(event -> (SingleCardEvent) event).
					filter(event -> !event.getCard().getController().equals(source.getController())).
					collect(Collectors.toList());
		}
		
		return turnEvents;
	}

}