package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.events.Effect;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.events.Event;

/**
 * Damageable, Buffable, Refreshable / Exhaustable
 */
public abstract class DamageableCard extends Card {
    protected final int health;
    protected int damageTaken;
    protected boolean dead;

    protected boolean exhausted;
    protected boolean refreshable;

    protected List<Buff> buffs;

    protected DamageableCard(String name, ResourceList cost, String text, String flavorText, int health, Map<Trigger, List<Effect>> effects) {
        super(name, cost, text, flavorText, effects);
        this.health = health;
        this.damageTaken = 0;
        this.exhausted = false;
        this.refreshable = true;
        this.dead = false;
        buffs = new ArrayList<Buff>();
    }

    public DamageableCard(DamageableCard other) {
        super(other);
        this.health = other.health;
        this.damageTaken = other.damageTaken;
        this.exhausted = other.exhausted;
        this.refreshable = other.refreshable;
        this.buffs = other.buffs;
    }

    public Event takeDamage(int damage, Card source) {
        damageTaken += damage;
        checkDeath();
        return new Event(this.getName() + " took " + damage + " damage from " + source.getName());
    }

    public void takeDamageSingleTarget(int damage, Card source, State state) {
        Event damageEvent = takeDamage(damage, source);
        state.addEvent(damageEvent);
    }
    
    public Event heal(int heal, Card source) {
    	heal = Math.min(heal, getMaxHealth() - getCurrentHealth());
    	damageTaken -= heal;
    	return new Event(this.getName() + " healed " + heal + " damage from " + source.getName());
    }

    @ExposeMethodResult("currentHealth")
    public int getCurrentHealth() {
        return health - damageTaken; //@Todo: account for buffs later
    }

    @ExposeMethodResult("maxHealth")
    public int getMaxHealth() {
        return health; //@Todo: account for buffs
    }
    
    public void checkDeath() {
        if(getCurrentHealth() <= 0)
        	this.dead = true;
    }
    
    public void setDead(boolean dead) {
    	this.dead = dead;
    }

    public boolean isDead() {
        return dead;
    }

    public void exhaust() {
        exhausted = true;
    }

    public void attemptRefresh() {
        if(refreshable) {
            exhausted = false;
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isExhausted() {
        return exhausted;
    }

    public void setExhausted(boolean exhausted) {
        this.exhausted = exhausted;
    }

    public boolean isRefreshable() {
        return refreshable;
    }

    public void setRefreshable(boolean refreshable) {
        this.refreshable = refreshable;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public void addBuff(Buff buff) {
        buffs.add(buff);
        checkDeath();
    }
    
    public void removeBuff(Buff buff) {
    	buffs.remove(buff);
    	checkDeath();
    }
    
    public void reset() {
        this.refreshable = true;
        this.damageTaken = 0;
        this.exhausted = false;
        buffs.clear();
    }
}
