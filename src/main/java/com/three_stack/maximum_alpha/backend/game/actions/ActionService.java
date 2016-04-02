package com.three_stack.maximum_alpha.backend.game.actions;

public class ActionService {

    public static Class getAction(String actionName) throws ClassNotFoundException {
        String packageName = ActionService.class.getPackage().getName();
        String className = packageName + ".implementations." + actionName.replace(" ", "") + "Action";
        return Class.forName(className);
    }
}
