package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.database_client.pojos.DBEffect;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Effect {
    protected Card source;
    protected List<Check> checks;
    protected List<Result> results;
    protected List<Object> values;

    public Effect(DBEffect dbEffect) {
        this.checks = dbEffect.getChecks().stream()
                .map(Checks::getCheck)
                .collect(Collectors.toList());
        this.results = dbEffect.getResults().stream()
                .map(Results::getResult)
                .collect(Collectors.toList());
        this.values = dbEffect.getValues();
    }

    public Card getSource() {
        return source;
    }

    public void setSource(Card source) {
        this.source = source;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public void setChecks(List<Check> checks) {
        this.checks = checks;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
