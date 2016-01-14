package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.State;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    public static void notify(Trigger trigger, State state, Event event) {
        List<Effect> effects = state.getEffects(trigger);
        if(effects == null) {
            return;
        }

        //add triggered effects to the queue of triggered effects
        effects.stream()
                .filter(effect -> effect.checks.parallelStream().allMatch(check -> check.run(state, effect, event)))
                .map(effect -> new TriggeredEffect(effect, event))
                .forEach(state::addTriggeredEffect);
    }
}
