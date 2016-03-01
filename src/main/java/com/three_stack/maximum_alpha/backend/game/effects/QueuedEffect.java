package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.Step;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueuedEffect {
    protected Event triggeringEvent;
    protected Effect effect;
    protected int resultIndex;
    protected int stepIndex;
    /**
     * One value per result in an effect.
     */
    protected List<Map<String, Object>> values;

    public enum Type {
        DEFAULT,
        PREPARE_ONLY,
        RESOLVE_ONLY
    }
    protected Type type;

    public QueuedEffect(Event triggeringEvent, Effect effect, Type type) {
        setup(triggeringEvent, effect, type);
    }

    public QueuedEffect(Event triggeringEvent, Effect effect) {
        setup(triggeringEvent, effect, Type.DEFAULT);
    }

    protected void setup(Event triggeringEvent, Effect effect, Type type) {
        this.resultIndex = 0;
        this.stepIndex = 0;
        this.triggeringEvent = triggeringEvent;
        this.effect = effect;
        this.type = type;

        this.values = effect.getResults().stream()
                .map(Result::prepareNewValue)
                .collect(Collectors.toList());
    }

    public boolean isDone() {
        return resultIndex >= effect.getResults().size();
    }

    /**
     *
     * @param state
     * @param source
     * @param event
     * @return whether a prompt was created
     */
    public boolean run(State state, Card source, Event event) {
        for(; resultIndex < effect.getResults().size(); resultIndex++) {
            Result result = effect.getResults().get(resultIndex);
            if(!type.equals(Type.RESOLVE_ONLY)) {
                for(; stepIndex < result.getPreparationSteps().size(); stepIndex++) {
                    Step step = result.getPreparationSteps().get(stepIndex);
                    boolean promptCreated = step.run(state, source, event, values.get(resultIndex));
                    if(promptCreated) {
                        //stop early since we need to wait for the prompt
                        stepIndex++;
                        return true;
                    }
                }
                //not setting to zero in the for loop because run can halt early to wait for user prompt input and then resume later
                stepIndex = 0;
            }
            if(!type.equals(Type.PREPARE_ONLY)) {
                result.resolve(state, source, event, values.get(resultIndex));
            }
        }
        return false;
    }

    /**
     * auto-generated getters and setters
     */

    public Event getTriggeringEvent() {
        return triggeringEvent;
    }

    public void setTriggeringEvent(Event triggeringEvent) {
        this.triggeringEvent = triggeringEvent;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
