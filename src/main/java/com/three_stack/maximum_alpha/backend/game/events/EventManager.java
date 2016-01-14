package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.State;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    public static void notify(Trigger trigger, State state, Event event) {
        List<Effect> effects = getEffects(trigger, state);

        effects.stream()
                .filter(effect -> effect.checks.parallelStream().allMatch(check -> check.run(state, effect, event)))
                .forEach(effect -> {
                    //index tracks which result / value pair we're on so we can pass the proper value to the run() method
                    int[] index = {0};
                    effect.results.stream().forEachOrdered(result -> result.run(state, effect, event, effect.getValues().get(index[0]++)));
                });
    }

    private static List<Effect> getEffects(Trigger trigger, State state) {
        List<Effect> effects = state.getEffects(trigger);
        if (effects == null) {
            return new ArrayList<>();
        }
        //@Todo: implement cascading effects here
        return effects;
    }
}
