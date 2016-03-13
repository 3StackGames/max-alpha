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
    protected transient final int defaultAttack;
    protected Structure attackTarget;
    protected transient List<String> reasonsCanNotAttack;
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
    protected transient List<Creature> blockableCreatures;
    /**
     * Structures this creature can attack.
     * IMPORTANT: Only used and/or reliable during attack phase.
     */
    protected transient List<Structure> attackableStructures;

    public Creature(String name, ResourceList cost, String text, String flavorText, int defaultAttack, int health) {
        super(name, cost, text, flavorText, health);
        this.defaultAttack = defaultAttack;
        attackTarget = null;
        reasonsCanNotAttack = new ArrayList<>();
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
        this.defaultAttack = otherCreature.defaultAttack;
        this.attackTarget = otherCreature.attackTarget;
        this.reasonsCanNotAttack = otherCreature.reasonsCanNotAttack;
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
        if(!isBlocking()) {
            throw new IllegalStateException("blockTarget must be set");
        }

        Event a = this.takeDamage(blockTarget.getCurrentAttack(), blockTarget, battleTime, state);
        Event b = blockTarget.takeDamage(this.getCurrentAttack(), this, battleTime, state);
        state.addEvent(a, Trigger.ON_DAMAGE);
        state.addEvent(b, Trigger.ON_DAMAGE);

        blockTarget.clearAttackTarget();
        blockTarget.exhaust(exhaustTime, state);
        clearBlockTarget();
    }

    public int getDefaultAttack() {
        return defaultAttack;
    }

    //@Todo: work with arjun to make sure both this (currentAttack) and attack show up
    @ExposeMethodResult("currentAttack")
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
        return reasonsCanNotAttack.isEmpty() && !(exhausted || summoningSickness) && !isAttacking();
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
        attackableStructures = new ArrayList<>();
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
		reasonsCanNotAttack = new ArrayList<>();
		canBlock = true;
        summoningSickness = true;
	}
	
	@Override
	public void addBuff(Buff buff, Time time, State state) {
		super.addBuff(buff, time, state);
		buffAttack += buff.getAttackModifier();
	}
	
	@Override
	public void removeBuff(Buff buff, Time time, State state) {
		super.removeBuff(buff, time, state);
		buffAttack -= buff.getAttackModifier();
	}
	
	@Override
	public void buffReset(State state) {
		super.buffReset(state);
		buffAttack = 0;
	}

    @Override
    public void addTag(Tag tag) {
        super.addTag(tag);
        switch (tag.getType()) {
            case QUICK:
                setSummoningSickness(false);
                break;
            case GUARD:
                reasonsCanNotAttack.add("GUARD");
                break;
            default:
                break;
        }
    }

    @Override
    public void removeTag(Tag tag) {
        super.removeTag(tag);
        switch (tag.getType()) {
            case QUICK:
                if(isSummonedThisTurn()) {
                    setSummoningSickness(true);
                }
                break;
            case GUARD:
                reasonsCanNotAttack.remove("GUARD");
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

    public List<Structure> getAttackableStructures() {
        return attackableStructures;
    }

    public void setAttackableStructures(List<Structure> attackableStructures) {
        this.attackableStructures = attackableStructures;
    }

    @ExposeMethodResult("attackableStructureIds")
    public List<UUID> getAttackableStructureIds() {
        return getAttackableStructures().stream()
                .map(Structure::getId)
                .collect(Collectors.toList());
    }
}
