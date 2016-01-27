package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.events.SourceTargetEvent;
import io.gsonfire.annotations.ExposeMethodResult;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Creature extends NonSpellCard implements Worker {
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

    /**
     * @param state
     * @return
     */
    public void attack(Time battleTime, Time exhaustTime, State state) {
        if(isBlocked()) {
            throw new IllegalStateException("must not be blocked");
        } else if(!isAttacking()) {
            throw new IllegalStateException("must be attacking");
        }
        dealDamage(attackTarget, this.getCurrentAttack(), battleTime, state);
        exhaust(exhaustTime, state);
        clearAttackTarget();
    }

    public void block(Time battleTime, Time exhaustTime, State state) {
        if(isBlocking()) {
            throw new IllegalStateException("blockTarget must be set");
        }

        Event a = this.takeDamage(blockTarget.getCurrentAttack(), blockTarget, battleTime);
        Event b = blockTarget.takeDamage(this.getCurrentAttack(), this, battleTime);
        state.addEvent(a, Trigger.ON_DAMAGE);
        state.addEvent(b, Trigger.ON_DAMAGE);

        blockTarget.clearAttackTarget();
        clearBlockTarget();
        blockTarget.exhaust(exhaustTime, state);
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

    public void clearAttackTarget() {
        attackTarget = null;
    }
    public void setAttackTarget(Structure structure, Time time, State state) {
        attackTarget = structure;
        SourceTargetEvent attackEvent = new SourceTargetEvent(time, "attack", this, structure);
        state.addEvent(attackEvent, Trigger.ON_DECLARE_ATTACK);
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

    public void clearBlockTarget() {
        blockTarget = null;
    }

    public void setBlockTarget(Creature creature, Time time, State state) {
        blockTarget = creature;
        creature.addBlocker(this);
        SourceTargetEvent blockEvent = new SourceTargetEvent(time, "block", this, creature);
        state.addEvent(blockEvent, Trigger.ON_DECLARE_BLOCK);
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

	@Override
	public void reset() {
		super.reset();
		attackTarget = null;
		blockTarget =null;
		canAttack = true;
		canBlock = true;
        hasSummoningSickness = true;
	}
}
