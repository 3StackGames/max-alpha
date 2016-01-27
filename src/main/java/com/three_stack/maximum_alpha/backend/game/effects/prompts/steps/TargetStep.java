package com.three_stack.maximum_alpha.backend.game.effects.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TargetStep extends Step implements OptionContainer {
    protected transient List<NonSpellCard> targetables;
    protected transient NonSpellCard target;

    public TargetStep(String instruction, Object value, List<NonSpellCard> targetables) {
        super(instruction, value, null, null);
        this.targetables = targetables;
    }

    public TargetStep(String instruction, Object value, StepInputChecker stepInputChecker, List<NonSpellCard> targetables) {
        super(instruction, value, stepInputChecker, null);
        this.targetables = targetables;
    }

    public TargetStep(String instruction, Object value, StepCompleter stepCompleter, List<NonSpellCard> targetables) {
        super(instruction, value, null, stepCompleter);
        this.targetables = targetables;
    }

    public TargetStep(String instruction, Object value, StepInputChecker stepInputChecker, StepCompleter stepCompleter, List<NonSpellCard> targetables) {
        super(instruction, value, stepInputChecker, stepCompleter);
        this.targetables = targetables;
    }

    @Override
    public void complete(Card input, Prompt prompt) {
        setTarget((NonSpellCard) input);
        if(stepCompleter != null) stepCompleter.run(this, input, prompt);
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

    @Override
    public boolean isValidOption(Card needle) {
        return targetables.contains(needle);
    }

    @Override
    public void removeOption(Card card) {
        targetables.remove(card);
    }

    @Override
    public boolean isOptionSelected() {
        return target != null;
    }
}
