package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.State;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    public static void notify(Trigger trigger, State state, Event event) {
        List<Effect> effects = getEffects(trigger, state);

        for(Effect effect : effects) {
            if(effect.check.run(state, effect, event)) {
                effect.result.run(state, effect, event, effect.value);
            }
        }
    }

    private static List<Effect> getEffects(Trigger trigger, State state) {
        List<Effect> effects = state.getEffects(trigger);
        if(effects == null) {
            return new ArrayList<>();
        }
        //@Todo: implement cascading effects here
        return effects;
    }
}
