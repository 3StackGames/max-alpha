package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.Step;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetStep;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

import java.util.List;
import java.util.Map;

public class HealResult extends TargetResult {
    protected int heal;

    public HealResult(List<Step> steps, int heal) {
        super(steps);
        this.heal = heal;
    }

    public HealResult(DBResult dbResult) {
        super(dbResult);
        this.heal = (int) dbResult.getValue().get("heal");
    }

    public HealResult(Result other) {
        super(other);
        HealResult otherHealResult = (HealResult) other;
        this.heal = otherHealResult.heal;
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value =  super.prepareNewValue();
        value.put("heal", heal);
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resolve(State state, Card source, Event event, Map<String, Object> value) {
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        int heal = (int) value.get("heal");
        source.heal(targets, heal, state.getTime(), state);
    }
}
