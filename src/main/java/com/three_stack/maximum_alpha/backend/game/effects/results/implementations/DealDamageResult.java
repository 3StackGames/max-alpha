package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetResult;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

import java.util.List;
import java.util.Map;

public class DealDamageResult extends TargetResult {
    protected int damage;

    public DealDamageResult(DBResult dbResult) {
        super(dbResult);
        this.damage = (int) dbResult.getValue().get("damage");
    }

    public DealDamageResult(Result other) {
        super(other);
        DealDamageResult otherDealDamageResult = (DealDamageResult) other;
        this.damage = otherDealDamageResult.damage;
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value =  super.prepareNewValue();
        value.put("damage", damage);
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resolve(State state, Card source, Event event, Map<String, Object> value) {
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        int damage = (int) value.get("damage");
        source.dealDamage(targets, damage, state.getTime(), state);
    }
}
