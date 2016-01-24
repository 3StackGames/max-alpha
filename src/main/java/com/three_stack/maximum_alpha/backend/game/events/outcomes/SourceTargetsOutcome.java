package com.three_stack.maximum_alpha.backend.game.events.outcomes;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SourceTargetsOutcome extends Outcome {
    protected transient Card source;
    protected transient List<Card> targets;

    public SourceTargetsOutcome(Card source, List<Card> targets) {
        super();
        this.source = source;
        this.targets = targets;
    }

    public SourceTargetsOutcome(Card source, Card target) {
        super();
        this.source = source;
        this.targets = new ArrayList<>();
        addTarget(target);
    }

    public Card getSource() {
        return source;
    }

    public void setSource(Card source) {
        this.source = source;
    }

    public void addTarget(Card target) {
        this.targets.add(target);
    }

    public List<Card> getTargets() {
        return targets;
    }

    public void setTargets(List<Card> card) {
        this.targets = targets;
    }

    @ExposeMethodResult("targetIds")
    public List<UUID> getTargetId() {
        return targets.stream().map(Card::getId).collect(Collectors.toList());
    }

    @ExposeMethodResult("sourceId")
    public UUID getSourceId() {
        return source.getId();
    }
}
