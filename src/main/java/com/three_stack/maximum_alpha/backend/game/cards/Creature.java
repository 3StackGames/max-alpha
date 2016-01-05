package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.Player;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.events.Event;

public class Creature extends DamageableCard implements Worker {
    //@Todo: Add Classes / Roles
    //@Todo: Add Tags
    protected final int attack;
    protected Structure attackTarget;
    protected boolean canAttack;
    protected Creature blockTarget;
    protected boolean canBlock;
    protected boolean hasSummoningSickness;

    protected Creature(String name, ResourceList cost, String text, String flavorText, int attack, int health) {
        super(name, cost, text, flavorText, health);
        this.attack = attack;
        attackTarget = null;
        canAttack = true;
        blockTarget = null;
        canBlock = true;
        hasSummoningSickness = true;
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

        aggressor.setAttackTarget(null);
        setBlockTarget(null);
        aggressor.exhaust();
        exhaust();

        return Event.joinEvents(a, b);
    }

    public int getDefaultAttack() {
        return attack;
    }

    public int getCurrentAttack() {
        //@Todo: reflect buffs
        return getDefaultAttack();
    }

    @Override
    //@Todo: needs refactoring - player.assign(this)?
    public Event assign(State state, Player player) {
        player.getHand().remove(this);
        player.getWorkers().add(this);
        return new Event(name + " was assigned to player " + player.getUsername());
    }

    public boolean hasSummoningSickness() {
        return hasSummoningSickness;
    }

    public void setHasSummoningSickness(boolean hasSummoningSickness) {
        this.hasSummoningSickness = hasSummoningSickness;
    }

    public Structure getAttackTarget() {
        return attackTarget;
    }

    public boolean isAttacking() {
        return attackTarget == null;
    }

    public void setAttackTarget(Structure c) {
        attackTarget = c;
    }

    public boolean canAttack() {
        return !(exhausted || hasSummoningSickness);
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public Creature getBlockTarget() {
        return blockTarget;
    }

    public boolean isBlocking() {
        return blockTarget == null;
    }

    public void setBlockTarget(Creature c) {
        blockTarget = c;
    }

    public boolean canBlock() {
        return !exhausted;
    }

    public void setCanBlock(boolean canBlock) {
        this.canBlock = canBlock;
    }
}
