package com.three_stack.maximum_alpha.backend.game.cards;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import io.gsonfire.annotations.ExposeMethodResult;

public class Buff {
	final int attackModifier;
	final int healthModifier;
	final Map<Trigger, List<Effect>> effects;
	final transient Card source;
	final boolean isAura;
	final String name;
	
	public Buff(int attack, int health, Map<Trigger, List<Effect>> effects, Card source, boolean isAura, String name) {
		this.attackModifier = attack;
		this.healthModifier = health;
		this.effects = effects;
		this.source = source;
		this.isAura = isAura;
		this.name = name;
	}
	
	public boolean isAura() {
		return isAura;
	}

	public int getHealthModifier() {
		return healthModifier;
	}

	public Map<Trigger, List<Effect>> getEffects() {
		return effects;
	}

	public Card getSource() {
		return source;
	}
	
	public int getAttackModifier() {
		return attackModifier;
	}
	
	public void onAdd(NonSpellCard c, State s) {
		if(effects != null)
			for(Trigger t : effects.keySet()) {
				for(Effect e : effects.get(t)) {
					s.addEffect(t, e);
				}
			}
	}
	
	public void onRemove(NonSpellCard c, State s) {
		if(effects != null)
			for(Trigger t : effects.keySet()) {
				for(Effect e : effects.get(t)) {
					s.removeEffect(t, e);
				}
			}
	}

	@ExposeMethodResult("sourceId")
	public UUID getSourceId() {
		return source.getId();
	}
	
	public String getName() {
		return name;
	}
}
