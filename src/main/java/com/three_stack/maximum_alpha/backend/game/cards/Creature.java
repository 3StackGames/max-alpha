package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.event.Event;

public class Creature extends DamageableCard implements Worker{
    //@Todo: Add Classes / Roles
    //@Todo: Add Tags
    protected final int attack;

    protected Creature(String name, ResourceList cost, String text, String flavorText, int attack, int health) {
        super(name, cost, text, flavorText, health);
        this.attack = attack;
    }

    @Override
    public ResourceList work(State state) {
        ResourceList resourceList = new ResourceList();
        resourceList.addColor(getDominantColor(), 1);
        return resourceList;
    }

    public Event block(Creature aggressor) {
        Event a = this.takeDamage(aggressor.getCurrentAttack(), this);
        Event b = aggressor.takeDamage(this.getCurrentAttack(), aggressor);

        return Event.joinEvents(a, b);
    }

    public int getDefaultAttack() {
        return attack;
    }

    public int getCurrentAttack() {
        //@Todo: reflect buffs
        return getDefaultAttack();
    }
}
