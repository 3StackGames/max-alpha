package com.three_stack.maximum_alpha.backend.game.actions;

import com.three_stack.maximum_alpha.backend.game.actions.implementations.*;

import java.util.HashMap;
import java.util.Map;

public class ActionService {
    private static final Map<String, Class> actionMap;

    public static Class getAction(String actionName) {
        return actionMap.get(actionName);
    }

    static {
        actionMap = new HashMap<>();
        actionMap.put("Play Card", PlayCardAction.class);//done
        actionMap.put("Assign Card", AssignCardAction.class);//done
        actionMap.put("Pull Card", PullCardAction.class);//implemented but not tested
        actionMap.put("Activate Effect", ActivateEffectAction.class);
        actionMap.put("Choose", ChooseAction.class);
        actionMap.put("Target", TargetAction.class);
        actionMap.put("Declare Attacker", DeclareAttackerAction.class);
        actionMap.put("Declare Blocker", DeclareBlockerAction.class);
        actionMap.put("Finish Phase", FinishPhaseAction.class);
        actionMap.put("End Turn Without Combat", EndTurnWithoutCombatAction.class);
        actionMap.put("Build Structure", BuildStructureAction.class);
    }
}
