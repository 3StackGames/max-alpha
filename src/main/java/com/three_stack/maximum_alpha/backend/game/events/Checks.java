package com.three_stack.maximum_alpha.backend.game.events;

public class Checks {
    public static Check getCheck(String checkName) {
        try {
            return (Check) Checks.class.getField(checkName).get(Checks.class.newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        //@Todo: handle case where it's not found.
        return null;
    }

    public static Check IS_SELF = (state, effect, event) -> {
        SingleCardEvent singleCardEvent = (SingleCardEvent) event;
        return effect.getSource().equals(singleCardEvent.getCard());
    };
}
