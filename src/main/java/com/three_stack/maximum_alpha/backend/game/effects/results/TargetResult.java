package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TargetResult extends Result{

    private List<Step> generateTargetSteps(DBResult dbResult) {
        List<Map<String, Object>> targetMaps = (List<Map<String, Object>>) dbResult.getValue().get("targets");
        List<TargetStep> targetSteps = targetMaps.stream()
                .map(targetMap -> new TargetStep(this, targetMap))
                .collect(Collectors.toList());
        return null;
    }
    public TargetResult(List<Step> steps) {
        super(steps);
    }

    @SuppressWarnings("unchecked")
    public TargetResult(DBResult dbResult) {
        super(dbResult);
        //parse targets to create steps (including prompts)
        List<Map<String, Object>> targetMaps = (List<Map<String, Object>>) dbResult.getValue().get("targets");
        List<Step> targetSteps = targetMaps.stream()
                .map(targetMap -> new TargetStep(this, targetMap))
                .collect(Collectors.toList());
    }

    public TargetResult(Result other) {
        super(other);
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value = super.prepareNewValue();
        value.put("targets", new ArrayList<NonSpellCard>());
        return value;
    }
}
