package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.Step;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class QueuedEffect {
    protected Event triggeringEvent;
    protected Effect effect;
    protected int promptStepIndex;
    /**
     * One value per result in an effect.
     * key: result ID
     * value: result's value map
     */
    protected Map<UUID, Map<String, Object>> values;

    /**
     * Used to distinguish between when an effect is run normally vs during Preparation and Damage phase
     */
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
        this.promptStepIndex = 0;
        this.triggeringEvent = triggeringEvent;
        this.effect = effect;
        this.type = type;
        this.values = effect.getResults().stream()
                .collect(Collectors.toMap(
                        Result::getId,
                        Result::prepareNewValue
                ));
    }

    /**
     * prompts should get run before the effect is resolved
     * @param state
     * @param source
     * @param event
     * @return
     */
    public boolean runPrompt(State state, Card source, Event event) {
        if(!type.equals(Type.RESOLVE_ONLY)) {
            List<Step> allPromptSteps = effect.getResults().stream()
                    .map(Result::getPromptSteps)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            for(; promptStepIndex < allPromptSteps.size(); promptStepIndex++) {
                Step promptStep = allPromptSteps.get(promptStepIndex);
                promptStep.run(state, source, event, values.get(promptStep.getResult().getId()));
                //prompt will always be created
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param state
     * @param source
     * @param event
     * @return whether a prompt was created
     */
    public boolean runAutoTargetStepsAndResolve(State state, Card source, Event event) {
        /**
         * Handle all auto targeting and resolve
         */
        for(int resultIndex = 0; resultIndex < effect.getResults().size(); resultIndex++) {
            Result result = effect.getResults().get(resultIndex);
            if(!type.equals(Type.RESOLVE_ONLY)) {
                for(int autoTargetStepIndex = 0; autoTargetStepIndex < result.getPromptSteps().size(); autoTargetStepIndex++) {
                    Step step = result.getPromptSteps().get(autoTargetStepIndex);
                    step.run(state, source, event, values.get(result.getId()));
                }
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
