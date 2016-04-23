package com.three_stack.maximum_alpha.backend.game.effects.results;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

public abstract class PlayerResult extends Result {
	public PlayerResult(List<PlayerStep> playerSteps) {
        super();
        preparationSteps.addAll(playerSteps);
    }

    @SuppressWarnings("unchecked")
    public PlayerResult(DBResult dbResult) {
        super(dbResult);
        //parse players to create steps (including prompts)
        List<Map<String, Object>> playerMaps = (List<Map<String, Object>>) dbResult.getValue().get("players");
        List<PlayerStep> playerSteps = playerMaps.stream()
                .map(PlayerStep::new)
                .collect(Collectors.toList());
        preparationSteps.addAll(playerSteps);
    }

    public PlayerResult(Result other) {
        super(other);
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value = super.prepareNewValue();
        value.put("players", new ArrayList<Player>());
        return value;
    }
}
