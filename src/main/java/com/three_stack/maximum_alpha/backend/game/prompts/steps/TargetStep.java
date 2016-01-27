package com.three_stack.maximum_alpha.backend.game.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.Step;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TargetStep extends Step {
    protected transient List<NonSpellCard> targetables;

    protected transient NonSpellCard target;

    public TargetStep(String instruction, List<NonSpellCard> targetables) {
        super(instruction);
        this.targetables = targetables;
    }

    public TargetStep(String instruction, Object value, List<NonSpellCard> targetables) {
        super(instruction, value);
        this.targetables = targetables;
    }

    @Override
    public void complete(Card target) {
        setTarget((NonSpellCard) target);
    }

    public List<NonSpellCard> getTargetables() {
        return targetables;
    }

    public void setTargetables(List<NonSpellCard> targetables) {
        this.targetables = targetables;
    }

    public NonSpellCard getTarget() {
        return target;
    }

    public void setTarget(NonSpellCard target) {
        this.target = target;
    }
    
    public void reset() {
    	target = null;
    }

    @ExposeMethodResult("type")
    public String getType() {
        return "TARGET";
    }

    @ExposeMethodResult("targetableIds")
    public List<UUID> getTargetableIds() {
        return targetables.stream().map(Card::getId).collect(Collectors.toList());
    }

    @ExposeMethodResult("targetId")
    public UUID getTargetId() {
        if(target == null) return null;
        return target.getId();
    }
}
