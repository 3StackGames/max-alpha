package com.three_stack.maximum_alpha.backend.game.cards;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.player.ResourceList;
import com.three_stack.maximum_alpha.database_client.pojos.DBAbility;

public class Ability {
    protected final UUID id;
    protected final ResourceList cost;
    protected final boolean tap;
    protected final String text;
    protected final transient List<Effect> effects;

    public Ability(Card finalCard, DBAbility dbAbility) {
        this.id = UUID.randomUUID();
        this.cost = new ResourceList(dbAbility.getCost());
        this.tap = dbAbility.isTap();
        this.text = dbAbility.getText();
        this.effects = dbAbility.getEffects().stream()
                .map(dbEffect -> new Effect(finalCard, dbEffect))
                .collect(Collectors.toList());
    }

    public Ability(Card finalCard, ResourceList cost, boolean tap, String text, List<Effect> effects) {
        this.id = UUID.randomUUID();
        this.cost = cost;
        this.tap = tap;
        this.text = text;
        this.effects = effects;
    }

    public Ability(Ability other) {
        this.id = UUID.randomUUID();
        this.cost = new ResourceList(other.cost);
        this.tap = other.tap;
        this.text = other.text;
        this.effects = other.effects.stream()
                .map(Effect::new)
                .collect(Collectors.toList());
    }

    public ResourceList getCost() {
        return cost;
    }

    public boolean isTap() {
        return tap;
    }

    public String getText() {
        return text;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public UUID getId() {
        return id;
    }
}
