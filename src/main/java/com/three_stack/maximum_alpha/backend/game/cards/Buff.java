package com.three_stack.maximum_alpha.backend.game.cards;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.events.Effect;

/**
 * @Todo: Do the following
 *  health
 *  attack
 *  attributes
 *  etc
 */
public class Buff {
	int attackModifier;
	int healthModifier;
	List<Effect> effects;
	Card source;
	
	public Buff(int attack, int health, List<Effect> effects, Card source) {
		this.attackModifier = attack;
		this.healthModifier = health;
		this.effects = effects;
		this.source = source;
	}
	
}
