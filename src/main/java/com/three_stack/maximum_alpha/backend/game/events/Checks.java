package com.three_stack.maximum_alpha.backend.game.events;

public class Checks {
    public static Check IS_SELF = (state, effect, event) -> {
        SingleCardEvent singleCardEvent = (SingleCardEvent) event;
        return effect.getSource().equals(singleCardEvent.getCard());
    };
}
