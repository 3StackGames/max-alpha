package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.events.SourceTargetEvent;
import io.gsonfire.annotations.ExposeMethodResult;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.State;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Creature extends NonSpellCard implements Worker {
    protected transient final int attack;
    protected Structure attackTarget;
    protected transient boolean canAttack;
    protected transient Creature blockTarget;
    protected transient boolean canBlock;
    protected boolean summoningSickness;
    protected boolean summonedThisTurn;
    protected transient List<Creature> blockers;
    private transient int buffAttack;
    /**
     * Creatures this creature can block.
     * IMPORTANT: Only used and/or reliable during block phase.
     */
    protected List<Creature> blockableCreatures;

    public Creature(String name, ResourceList cost, String text, String flavorText, int attack, int health) {
        super(name, cost, text, flavorText, health);
        this.attack = attack;
        attackTarget = null;
        canAttack = true;
        blockTarget = null;
        canBlock = true;
        summoningSickness = true;
        buffAttack = 0;
        summonedThisTurn = true;
        resetCombat();
    }

    public Creature(NonSpellCard other) {
        super(other);
        Creature otherCreature = (Creature) other;
        this.attack = otherCreature.attack;
        this.attackTarget = otherCreature.attackTarget;
        this.canAttack = otherCreature.canAttack;
        this.blockTarget = otherCreature.blockTarget;
        this.canBlock = otherCreature.canBlock;
        this.summoningSickness = otherCreature.summoningSickness;
        this.summonedThisTurn = otherCreature.summonedThisTurn;
        this.blockers = otherCreature.blockers;
        this.buffAttack = otherCreature.buffAttack;
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

    //@Todo: work with arjun to make sure both this (currentAttack) and attack show up
    @ExposeMethodResult("attack")
    public int getCurrentAttack() {
        return getDefaultAttack() + buffAttack;
    }

    public boolean hasSummoningSickness() {
        return summoningSickness;
    }

    public void setSummoningSickness(boolean summoningSickness) {
        this.summoningSickness = summoningSickness;
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
        return canAttack && !(exhausted || summoningSickness) && !isAttacking();
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public Creature getBlockTarget() {
        return blockTarget;
    }

    @ExposeMethodResult("blockTargetId")
    public UUID getBlockTargetID() {
        if(getBlockTarget() != null) {
            return getBlockTarget().getId();
        } else {
            return null;
        }
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

    public void resetCombat() {
        blockers = new ArrayList<>();
        blockableCreatures = new ArrayList<>();
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
	public void reset(State state) {
		super.reset(state);
		attackTarget = null;
		blockTarget = null;
		canAttack = true;
		canBlock = true;
        summoningSickness = true;
	}
	
	@Override
	public void addBuff(Buff buff, State state) {
		super.addBuff(buff, state);
		buffAttack += buff.getAttackModifier();
	}
	
	@Override
	public void removeBuff(Buff buff, State state) {
		super.removeBuff(buff, state);
		buffAttack -= buff.getAttackModifier();
	}
	
	@Override
	public void buffReset(State state) {
		super.buffReset(state);
		buffAttack = 0;
	}

    @Override
    public void processTag(Tag tag) {
        switch (tag.getType()) {
            case QUICK:
                setSummoningSickness(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void processTagRemoval(Tag tag) {
        switch (tag.getType()) {
            case QUICK:
                if(isSummonedThisTurn()) {
                    setSummoningSickness(true);
                }
                break;
            default:
                break;
        }
    }

    public boolean isSummonedThisTurn() {
        return summonedThisTurn;
    }

    public void setSummonedThisTurn(boolean summonedThisTurn) {
        this.summonedThisTurn = summonedThisTurn;
    }

    public List<Creature> getBlockableCreatures() {
        return blockableCreatures;
    }

    public void setBlockableCreatures(List<Creature> blockableCreatures) {
        this.blockableCreatures = blockableCreatures;
    }

    @ExposeMethodResult("blockableCreatureIds")
    public List<UUID> getBlockableCreatureIds() {
        return getBlockableCreatures().stream()
                .map(Creature::getId)
                .collect(Collectors.toList());
    }
}
