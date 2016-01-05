package com.three_stack.maximum_alpha.backend.game.actions;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.events.Event;
import org.json.JSONException;
import org.json.JSONObject;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

/**
 * Always player Input
 */
public abstract class Action extends Event {
    protected int playerId;

    abstract public Event run();


    public enum ActionType {
        PLAY_CARD, ASSIGN_CARD, PULL_CARD, ACTIVATE_EFFECT, CHOOSE_EFFECT, TARGET_EFFECT,
        DECLARE_ATTACKER, DECLARE_BLOCKER, FINISH_PHASE, END_TURN_WITHOUT_COMBAT, BUILD_STRUCTURE
    }


    public long getCombatTargetId() {
        return combatTargetId;
    }

    public void setCombatTargetId(long combatTargetId) {
        this.combatTargetId = combatTargetId;
    }

    public List<Card> choices;
    public List<Long> targets;
    public long actionCardId;
    public ActionType type;
    public ResourceList cost;
    public long combatTargetId;

    public ResourceList getCost() {
        return cost;
    }

    public void setCost(ResourceList cost) {
        this.cost = cost;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    //maybe refactor later
    public static boolean isValidAction(JSONObject action) throws JSONException {
        try {
            ActionType type = ActionType.valueOf(action.getString("type"));
            action.getLong("playerId");
            if (hasActionCardId(type)) {
                action.getLong("actionCard");
            }
            if (hasCost(type)) {
                action.getString("cost");
            }
            if (hasTargets(type)) {
                action.getString("targets");
            }
            if (hasChoices(type)) {
                action.getString("choices");
            }
            if (hasCombatTargetId(type)) {
                action.getString("combatTarget");
            }
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public static boolean hasActionCardId(ActionType type) {
        return type == ActionType.PLAY_CARD || type == ActionType.ASSIGN_CARD || type == ActionType.PULL_CARD || type == ActionType.ACTIVATE_EFFECT || type == ActionType.DECLARE_ATTACKER || type == ActionType.DECLARE_BLOCKER;
    }

    public static boolean hasCost(ActionType type) {
        return type == ActionType.PLAY_CARD || type == ActionType.ACTIVATE_EFFECT;
    }

    public static boolean hasChoices(ActionType type) {
        return type == ActionType.CHOOSE_EFFECT;
    }

    public static boolean hasTargets(ActionType type) {
        return type == ActionType.TARGET_EFFECT;
    }

    public static boolean hasCombatTargetId(ActionType type) {
        return type == ActionType.DECLARE_ATTACKER || type == ActionType.DECLARE_BLOCKER;
    }

    public List<Card> getChoices() {
        return choices;
    }

    public void setChoices(List<Card> choices) {
        this.choices = choices;
    }

    public List<Long> getTargets() {
        return targets;
    }

    public void setTargets(List<Long> targets) {
        this.targets = targets;
    }

    public long getActionCardId() {
        return actionCardId;
    }

    public void setActionCardId(long actionCardId) {
        this.actionCardId = actionCardId;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }
}
