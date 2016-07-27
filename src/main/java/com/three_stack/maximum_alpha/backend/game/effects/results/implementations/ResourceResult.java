package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.PlayerResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.PlayerStep;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.ResourceList;
import com.three_stack.maximum_alpha.backend.game.player.ResourceList.Color;
import com.three_stack.maximum_alpha.backend.game.utilities.ValueExpression;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

public class ResourceResult extends PlayerResult {
  	protected Map<String, ValueExpression> resources;
  	protected boolean set;

  	//if set == true, sets the player's resources to the new set of resources. 
  	//Otherwise, adds the new set of resources to the player's current resources.
    public ResourceResult(List<PlayerStep> playerSteps, Map<String, ValueExpression> resources, boolean set) {
        super(playerSteps);
        this.resources = resources;
        this.set = set;
    }

    public ResourceResult(DBResult dbResult) {
        super(dbResult);
        Map<String, ValueExpression> resourceMap = new HashMap<>();
        for(Color c : Color.values()) {
        	Object o = dbResult.getValue().get(c.toString().toLowerCase());
        	if(o != null)
        		resourceMap.put(c.toString(), new ValueExpression(o));
        }
        this.set = (boolean) dbResult.getValue().get("set");
    }

    public ResourceResult(Result other) {
        super(other);
        ResourceResult otherResult = (ResourceResult) other;
        this.resources = otherResult.resources;
        this.set = otherResult.set;
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value =  super.prepareNewValue();
        value.put("resources", resources);
        value.put("set", set);
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resolve(State state, Card source, Event event, Map<String, Object> value) {
        List<Player> players = (List<Player>) value.get("players");
        Map<String, ValueExpression> resources = (Map<String, ValueExpression>) value.get("resources");
        Map<String, Integer> resourceValues = 
            resources.entrySet().stream()
              .collect(Collectors.toMap(e -> e.getKey(),
                                        e -> e.getValue().eval(state, source)
            ));
        ResourceList resourceList = new ResourceList(resourceValues);
        
        for(Player player : players) {
        	if(set)
        		player.setResources(resourceList);
        	else
        		player.addResources(resourceList);
        }
    }
}
