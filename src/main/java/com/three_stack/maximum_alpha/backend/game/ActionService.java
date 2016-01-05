package com.three_stack.maximum_alpha.backend.game;

import java.util.HashMap;
import java.util.Map;

public class ActionService {
    private static final Map<String, Class> actionMap;

    public static Class getAction(String actionName) {
        return actionMap.get(actionName);
    }

    static {
        actionMap = new HashMap<>();
    }
}
