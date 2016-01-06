package com.three_stack.maximum_alpha.backend.game.cards;

import java.util.ArrayList;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.Buff;
import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.events.Event;

/**
 * Damageable, Buffable, Refreshable / Exhaustable
 */
public abstract class DamageableCard extends Card implements Damageable {
    protected final int health;
    protected int damageTaken;

    protected boolean exhausted;
    protected boolean refreshable;

    protected List<Buff> buffs;

    protected DamageableCard(String name, ResourceList cost, String text, String flavorText, int health) {
        super(name, cost, text, flavorText);
        this.health = health;
        this.refreshable = true;
        this.damageTaken = 0;
        this.exhausted = false;
        this.refreshable = true;
        buffs = new ArrayList<Buff>();
    }

    public Event takeDamage(int damage, Card source) {
        damageTaken += damage;
        return new Event(this.getName() + " took " + damage + " damage from " + source.getName());
    }

    public int getCurrentHealth() {
        return health - damageTaken; //@Todo: account for buffs later
    }

    public int getMaxHealth() {
        return health;
    }

    public boolean isAlive() {
        return getCurrentHealth() > 0;
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

    public void setBuffs(List<Buff> buffs) {
        this.buffs = buffs;
    }
}
