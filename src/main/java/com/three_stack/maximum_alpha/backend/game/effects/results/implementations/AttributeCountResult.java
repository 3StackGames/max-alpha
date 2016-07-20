package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.PlayerResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.PlayerStep;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

/**
 * Sets or adds to the count of some attribute.
 * Mostly for things like number of X done this game/turn.
 */
public class AttributeCountResult extends PlayerResult {
  protected String name;
  protected boolean set;
  protected int amount;

  //if set == true, sets the attribute count to the new value.
  //Otherwise, adds to the count.
  public AttributeCountResult(List<PlayerStep> playerSteps, String name, boolean set, int amount) {
      super(playerSteps);
      this.name = name;
      this.set = set;
      this.amount = amount;
  }

  public AttributeCountResult(DBResult dbResult) {
      super(dbResult);
      this.name = (String) dbResult.getValue().get("name");
      this.set = (boolean) dbResult.getValue().get("set");
      this.amount = (int) dbResult.getValue().get("amount");
  }

  public AttributeCountResult(Result other) {
      super(other);
      AttributeCountResult otherResult = (AttributeCountResult) other;
      this.name = otherResult.name;
      this.set = otherResult.set;
      this.amount = otherResult.amount;
  }

  @Override
  public Map<String, Object> prepareNewValue() {
      Map<String, Object> value =  super.prepareNewValue();
      value.put("name", name);
      value.put("set", set);
      value.put("amount", amount);
      return value;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void resolve(State state, Card source, Event event, Map<String, Object> value) {
      List<Player> players = (List<Player>) value.get("players");
      for (Player p : players) {
        if (set) {
          p.setAttributeCount(name, amount);
        }
        else {
          int newSum = p.getAttributeCount(name) + amount;
          p.setAttributeCount(name, newSum);
        }
      }
  }
}
