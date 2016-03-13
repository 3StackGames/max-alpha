package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TargetResult extends Result{
    public TargetResult(List<TargetStep> targetSteps) {
        super();
        preparationSteps.addAll(targetSteps);
    }

    @SuppressWarnings("unchecked")
    public TargetResult(DBResult dbResult) {
        super(dbResult);
        //parse targets to create steps (including prompts)
        List<Map<String, Object>> targetMaps = (List<Map<String, Object>>) dbResult.getValue().get("targets");
        List<TargetStep> targetSteps = targetMaps.stream()
                .map(TargetStep::new)
                .collect(Collectors.toList());
        preparationSteps.addAll(targetSteps);
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
