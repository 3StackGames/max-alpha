package com.three_stack.maximum_alpha.backend.game.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.Step;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TargetStep extends Step {
    protected transient List<Card> targetables;

    protected transient Card target;

    public TargetStep(String instruction, List<Card> targetables) {
        super(instruction);
        this.targetables = targetables;
    }

    @Override
    public void complete(Card target) {
        setTarget(target);
    }

    public List<Card> getTargetables() {
        return targetables;
    }

    public void setTargetables(List<Card> targetables) {
        this.targetables = targetables;
    }

    public Card getTarget() {
        return target;
    }

    public void setTarget(Card target) {
        this.target = target;
    }
    
    public void reset() {
    	target = null;
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
