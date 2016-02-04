package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.effects.*;
import com.three_stack.maximum_alpha.backend.game.effects.events.SingleCardEvent;
import com.three_stack.maximum_alpha.backend.game.effects.events.SourceDamageTargetEvent;

import io.gsonfire.annotations.ExposeMethodResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.ResourceList;

public abstract class NonSpellCard extends Card {
    protected final int health;
    protected int damageTaken;
    protected boolean dead;

    protected boolean exhausted;
    protected boolean refreshable;

    protected List<Buff> buffs;
    private transient int buffHealth;

    protected NonSpellCard(String name, ResourceList cost, String text, String flavorText, int health) {
        super(name, cost, text, flavorText);
        this.health = health;
        this.damageTaken = 0;
        this.exhausted = false;
        this.refreshable = true;
        this.dead = false;
        buffs = new ArrayList<>();
        buffHealth = 0;
    }

    public NonSpellCard(NonSpellCard other) {
        super(other);
        this.health = other.health;
        this.damageTaken = other.damageTaken;
        this.exhausted = other.exhausted;
        this.refreshable = other.refreshable;
        this.buffs = other.buffs;
    }

    public SourceDamageTargetEvent takeDamage(int damage, Card source, Time time) {
        damageTaken += damage;
        checkDeath();
        return new SourceDamageTargetEvent(time, source, this, damage);
    }

    public SingleCardEvent die(Time time, State state) {
        SingleCardEvent deathEvent = new SingleCardEvent(time, "death", this);
        state.addEvent(deathEvent, Trigger.ON_DEATH);
        return deathEvent;
    }

    @ExposeMethodResult("currentHealth")
    public int getCurrentHealth() {
        return getMaxHealth() - damageTaken;
    }

    @ExposeMethodResult("maxHealth")
    public int getMaxHealth() {
        return health + buffHealth;
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

    public void exhaust(Time time, State state) {
        if(exhausted) {
            //don't do anything
            return;
        }
        exhausted = true;
        state.createSingleCardEvent(this, "exhaust", time, Trigger.ON_EXHAUST);
    }

    public void attemptRefresh(Time time, State state) {
        if(refreshable && exhausted) {
            exhausted = false;
            state.createSingleCardEvent(this, "refresh", time, Trigger.ON_REFRESH);
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
    
    public List<Buff> getNonAuraBuffs() {
    	return buffs.stream().filter(buff -> !buff.isAura()).collect(Collectors.toList());
    }

    public void addBuff(Buff buff, State state) {
    	buff.onAdd(this, state);
        buffs.add(buff);
        buffHealth += buff.getHealthModifier();
        checkDeath();
    }
    
    public void removeBuff(Buff buff, State state) {
    	int idx = buffs.indexOf(buff);
    	for(int i = buffs.size()-1; i >= idx; i--) {
    		Buff curr = buffs.get(i);
        	curr.onRemove(this, state);
    	}
		for(int i = idx+1; i < buffs.size(); i++) {
    		Buff curr = buffs.get(i);
        	curr.onAdd(this, state);
		}
		buffs.remove(buff);
        buffHealth -= buff.getHealthModifier();
        
    	checkDeath();
    }
    
    public void reset(State state) {
    	buffReset(state); //do first
        this.refreshable = true;
        this.damageTaken = 0;
        this.exhausted = false;
    }
    
    public void buffReset(State state) {
    	for(int i = buffs.size()-1; i >= 0; i--) {
    		Buff buff = buffs.get(i);
    		buff.onRemove(this, state);
    	}
    	buffs.clear();
    	buffHealth = 0;
    }
}
