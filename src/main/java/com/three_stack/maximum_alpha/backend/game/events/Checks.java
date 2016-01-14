package com.three_stack.maximum_alpha.backend.game.events;

import org.apache.log4j.Logger;

public class Checks {
    static Logger log = Logger.getLogger(Checks.class.getName());

    public static Check getCheck(String checkName) {
        try {
            return (Check) Checks.class.getField(checkName).get(Checks.class.newInstance());
        } catch (Exception e) {
            log.warn("Tried getting check called " + checkName + " but failed. Falling back to FALSE");
            return FALSE;
        }
    }

    public static Check FALSE = (state, effect, event) -> false;

    public static Check IS_SELF = (state, effect, event) -> {
        SingleCardEvent singleCardEvent = (SingleCardEvent) event;
        return effect.getSource().equals(singleCardEvent.getCard());
    };
}
