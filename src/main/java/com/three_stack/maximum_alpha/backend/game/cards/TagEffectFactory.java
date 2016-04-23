package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.effects.Check;
import com.three_stack.maximum_alpha.backend.game.effects.Checks;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.Step;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetStep;
import com.three_stack.maximum_alpha.backend.game.effects.results.implementations.BuffResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.implementations.DealDamageResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagEffectFactory {
    public static Effect createDegenerateEffect(int damage, Card source) {
        List<Check> checks = new ArrayList<>();
        checks.add(Checks.S_ON_FIELD);
        checks.add(Checks.C_TURN_PLAYER);

        List<Step> steps = new ArrayList<>();
        DealDamageResult dealDamageResult = new DealDamageResult(steps, damage);
        List<Result> results = new ArrayList<>();
        Map<String, Object> targetStepMap = new HashMap<>();
        targetStepMap.put("self", true);
        TargetStep targetSelf = new TargetStep(dealDamageResult, targetStepMap);
        steps.add(targetSelf);

        results.add(dealDamageResult);

        Effect effect = new Effect(source, checks, results);
        return effect;
    }

    //@Todo: consolidate buffs rather than constantly adding new ones
    public static Effect createGrowthEffect(int attackRate, int healthRate, Card source) {
        List<Check> checks = new ArrayList<>();
        checks.add(Checks.S_ON_FIELD);
        checks.add(Checks.C_TURN_PLAYER);

        List<Result> results = new ArrayList<>();
        List<Step> steps = new ArrayList<>();
        Map<String, Object> targetStepMap = new HashMap<>();
        BuffResult buffResult = new BuffResult(steps, attackRate, healthRate);

        targetStepMap.put("self", true);
        TargetStep targetSelf = new TargetStep(buffResult, targetStepMap);
        steps.add(targetSelf);

        results.add(buffResult);

        Effect effect = new Effect(source, checks, results);
        return effect;
    }
}
