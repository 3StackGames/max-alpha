package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.Buff;
import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.events.Event;

import java.util.List;

public abstract class DamageableCard extends Card implements Damageable {
    protected final int health;
    protected int damageTaken;

    protected List<Buff> buffs;

    protected DamageableCard(String name, ResourceList cost, String text, String flavorText, int health) {
        super(name, cost, text, flavorText);
        this.health = health;
    }

    public Event takeDamage(int damage, Card source) {
        damageTaken += damage;
        return new Event(this.getName() + "took damage from" + source.getName());
    }

    public int getCurrentHealth() {
        return health - damageTaken; //@Todo: account for buffs later
    }

    public int getMaxHealth() {
        return health;
    }

    public boolean isDead() {
        return getCurrentHealth() <= 0;
    }


}
