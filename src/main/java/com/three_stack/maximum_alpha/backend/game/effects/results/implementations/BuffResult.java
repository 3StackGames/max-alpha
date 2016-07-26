package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Buff;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetStep;
import com.three_stack.maximum_alpha.backend.game.utilities.ValueExpression;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

import java.util.List;
import java.util.Map;

//TODO: implement buff effects + name
public class BuffResult extends TargetResult {
    protected ValueExpression attack;
    protected ValueExpression health;

    public BuffResult(List<TargetStep> targetSteps, ValueExpression attack, ValueExpression health) {
        super(targetSteps);
        this.attack = attack;
        this.health = health;
    }

    public BuffResult(DBResult dbResult) {
        super(dbResult);
        this.attack = new ValueExpression(dbResult.getValue().get("attack"));
        this.health = new ValueExpression(dbResult.getValue().get("health"));
    }

    public BuffResult(Result other) {
        super(other);
        BuffResult otherBuffResult = (BuffResult) other;
        this.attack = otherBuffResult.attack;
        this.health = otherBuffResult.health;
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        return super.prepareNewValue();
    }

    @Override
    public void resolve(State state, Card source, Event event, Map<String, Object> value) {
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        Buff buff = new Buff(attack.eval(state), health.eval(state), null, source, false, null);
        Time buffTime = state.getTime();
        targets.stream()
                .forEach(target -> target.addBuff(buff, buffTime, state));
    }
}
