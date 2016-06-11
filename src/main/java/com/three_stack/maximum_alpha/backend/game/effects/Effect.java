package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.ResultFactory;
import com.three_stack.maximum_alpha.backend.game.phases.PreparationPhase;
import com.three_stack.maximum_alpha.database_client.pojos.DBEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Effect {
    protected final UUID id;
    protected Card source;
    protected List<Check> checks;
    protected List<Result> results;

    public Effect(Card source, DBEffect dbEffect) {
        this.id = UUID.randomUUID();
        this.source = source;
        this.checks = dbEffect.getChecks().stream()
                .map(Checks::getCheck)
                .collect(Collectors.toList());
        this.results = dbEffect.getResults().stream()
                .map(ResultFactory::create)
                .collect(Collectors.toList());
    }

    public Effect(Card source, List<Check> checks, List<Result> results) {
        this.id = UUID.randomUUID();
        this.source = source;
        this.checks = checks;
        this.results = results;
    }

    public Effect(Card source) {
        this.id = UUID.randomUUID();
        this.source = source;
        this.checks = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public Effect(Effect other) {
        this.id = UUID.randomUUID();
        this.source = other.source;
        this.checks = other.checks.stream().collect(Collectors.toList());
        this.results = other.results.stream()
                .map(result -> {
                    try {
                        return result.getClass().getConstructor(Result.class).newInstance(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    public void addCheck(Check check) {
        getChecks().add(check);
    }

    public void addResult(Result result) {
        getResults().add(result);
    }

    public void trigger(Event event, State state) {
        QueuedEffect queuedEffect;
        if(state.isPhase(PreparationPhase.class)) {
            queuedEffect = new QueuedEffect(event, this, QueuedEffect.Type.PREPARE_ONLY);
            source.getController().pushPreparationPhaseQueuedRunnable(new QueuedEffect(event, this, QueuedEffect.Type.RESOLVE_ONLY));
        } else {
            queuedEffect = new QueuedEffect(event, this);
        }
        state.addQueuedEffect(queuedEffect);
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

    public UUID getId() {
        return id;
    }
}
