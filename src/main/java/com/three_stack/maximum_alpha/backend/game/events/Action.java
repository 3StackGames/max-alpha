package com.three_stack.maximum_alpha.backend.game.events;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

/**
 * Always player Input
 */
public class Action extends Event {
	public enum ActionType {
		PLAY_CARD, ASSIGN_CARD, PULL_CARD, ACTIVATE_EFFECT, CHOOSE_EFFECT, TARGET_EFFECT, 
		DECLARE_ATTACKER, DECLARE_BLOCKER, CHANGE_PHASE, SKIP_COMBAT, BUILD_STRUCTURE
	}
	
	public List<Card> choices;
	public List<Long> targets;
	public long actionCardId;
	public ActionType type;
	public ResourceList cost;
    
    public static boolean isValidAction(JSONObject action) throws JSONException {
    	try {
			ActionType type = ActionType.valueOf(action.getString("type"));
	    	if(hasActionCardId(type)) {
				action.getLong("actionCard");
			}
			if(hasCost(type)) {
				action.getString("cost");
			}
			return true;
    	}
		catch (JSONException e) {
			return false;
		}
    }

	public static boolean hasActionCardId(ActionType type) {
    	return type == ActionType.PLAY_CARD || type == ActionType.ASSIGN_CARD || type == ActionType.PULL_CARD || type == ActionType.ACTIVATE_EFFECT || type == ActionType.DECLARE_ATTACKER || type == ActionType.DECLARE_BLOCKER;
    }
	
	public static boolean hasCost(ActionType type) {
    	return type == ActionType.PLAY_CARD || type == ActionType.ACTIVATE_EFFECT;
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
