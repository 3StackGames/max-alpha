package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;

public interface Check {
    boolean run(State state, Effect effect, Event event);
}
