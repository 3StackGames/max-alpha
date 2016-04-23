package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

import java.util.*;
import java.util.stream.Collectors;

//@Todo: make this and all children immutable to avoid having to copy
public abstract class Result {
    protected final UUID id;
    protected List<Step> promptSteps;
    protected List<Step> preResolveSteps;

    /**
     * When using this constructor, be sure to call setSteps later on
     */
    public Result() {
        this.id = UUID.randomUUID();
        this.promptSteps = new ArrayList<>();
        this.preResolveSteps = new ArrayList<>();
    }

    public Result(List<Step> steps) {
        this.id = UUID.randomUUID();
        setSteps(steps);
    }

    /**
     * When using this constructor, be sure to call setSteps later on
     * @param dbResult
     */
    public Result(DBResult dbResult) {
        this.id = UUID.randomUUID();
    }

    public Result(Result other) {
        this.id = UUID.randomUUID();
        this.promptSteps = other.promptSteps.stream()
                .map(step -> {
                    try {
                        return step.getClass().getConstructor(Step.class).newInstance(step);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
        this.preResolveSteps = other.preResolveSteps.stream()
                .map(step -> {
                    try {
                        return step.getClass().getConstructor(Step.class).newInstance(step);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    protected void setSteps(List<Step> steps) {
        this.promptSteps = steps.stream()
                .filter(Step::isPrompt)
                .collect(Collectors.toList());
        this.preResolveSteps = steps.stream()
                .filter(step -> !step.isPrompt())
                .collect(Collectors.toList());
    }

    public abstract void resolve(State state, Card source, Event event, Map<String, Object> value);

    /**
     * adds necessary attributes to the value for a new result
     */
    public Map<String, Object> prepareNewValue() {
        return new HashMap<>();
    }

    public List<Step> getPromptSteps() {
        return promptSteps;
    }

    public List<Step> getPreResolveSteps() {
        return preResolveSteps;
    }

    public UUID getId() {
        return id;
    }
}
