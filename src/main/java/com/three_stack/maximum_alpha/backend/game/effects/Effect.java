package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.database_client.pojos.DBEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Effect {
    protected Card source;
    protected List<Check> checks;
    protected List<Result> results;
    protected List<Object> values;

    public Effect(Card source, DBEffect dbEffect) {
        this.source = source;
        this.checks = dbEffect.getChecks().stream()
                .map(Checks::getCheck)
                .collect(Collectors.toList());
        this.results = dbEffect.getResults().stream()
                .map(Results::getResult)
                .collect(Collectors.toList());
        this.values = dbEffect.getValues();
    }

    public Effect(Card source, List<Check> checks, List<Result> results, List<Object> values) {
        this.source = source;
        this.checks = checks;
        this.results = results;
        this.values = values;
    }

    public Effect(Card source) {
        this.source = source;
        this.checks = new ArrayList<>();
        this.results = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public void addCheck(Check check) {
        getChecks().add(check);
    }

    public void addResult(Result result) {
        getResults().add(result);
    }

    public void addValue(Object value) {
        getValues().add(value);
    }

    public void addResult(Result result, Object value) {
        addResult(result);
        addValue(value);
    }

    /**
     * Auto Generated Getters and Setters
     *
     */

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
