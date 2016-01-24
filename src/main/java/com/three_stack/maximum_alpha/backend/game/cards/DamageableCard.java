package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.events.*;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.SingleCardOutcome;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.SourceDamageTargetsOutcome;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.game.ResourceList;

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

    public SourceDamageTargetsOutcome takeDamage(int damage, Card source) {
        damageTaken += damage;
        checkDeath();
        return new SourceDamageTargetsOutcome(source, this, damage);
    }

    public Event die(State state) {
        Event deathEvent = new Event();
        deathEvent.addOutcome(new SingleCardOutcome("death", this));
        state.addEvent(deathEvent);
        state.notify(Trigger.ON_DEATH, deathEvent);
        return deathEvent;
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
