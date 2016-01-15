package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.events.Effect;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;
import io.gsonfire.annotations.ExposeMethodResult;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Creature extends DamageableCard implements Worker {
    //@Todo: Add Classes / Roles
    //@Todo: Add Tags
    protected final int attack;
    protected Structure attackTarget;
    protected transient boolean canAttack;
    protected Creature blockTarget;
    protected transient boolean canBlock;
    protected boolean hasSummoningSickness;
    protected List<Creature> blockers;

    public Creature(String name, ResourceList cost, String text, String flavorText, int attack, int health, Map<Trigger, List<Effect>> effects) {
        super(name, cost, text, flavorText, health, effects);
        this.attack = attack;
        attackTarget = null;
        canAttack = true;
        blockTarget = null;
        canBlock = true;
        hasSummoningSickness = true;
        resetBlockers();
    }

    @Override
    public ResourceList work(State state) {
        ResourceList resourceList = new ResourceList();
        resourceList.addColor(calculateDominantColor(), 1);
        return resourceList;
    }

    public Event block(Creature aggressor) {
        Event a = this.takeDamage(aggressor.getCurrentAttack(), this);
        Event b = aggressor.takeDamage(this.getCurrentAttack(), aggressor);

        aggressor.setAttackTarget(null);
        setBlockTarget(null);
        aggressor.exhaust();
        exhaust();

        return a.mergeEvent(b);
    }

    public int getDefaultAttack() {
        return attack;
    }

    public int getCurrentAttack() {
        //@Todo: reflect buffs
        return getDefaultAttack();
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
        return attackTarget != null;
    }

    public void setAttackTarget(Structure c) {
        attackTarget = c;
    }

    public boolean isBlocked() {
        return !blockers.isEmpty();
    }

    @ExposeMethodResult("canAttack")
    public boolean canAttack() {
        return canAttack && !(exhausted || hasSummoningSickness) && !isAttacking();
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public Creature getBlockTarget() {
        return blockTarget;
    }

    public boolean isBlocking() {
        return blockTarget != null;
    }

    public void setBlockTarget(Creature c) {
        blockTarget = c;
    }

    @ExposeMethodResult("canBlock")
    public boolean canBlock() {
        return canBlock && !exhausted && !isBlocking();
    }

    public void setCanBlock(boolean canBlock) {
        this.canBlock = canBlock;
    }

    public List<Creature> getBlockers() {
        return blockers;
    }

    public void resetBlockers() {
        blockers = new ArrayList<>();
    }

    public void addBlocker(Creature blocker) {
        blockers.add(blocker);
    }

	@Override
	public boolean isAssignable() {
		return true;
	}

	@Override
	public boolean isPullable() {
		return true;
	}


}
