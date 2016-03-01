package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.database_client.pojos.DBAbility;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Ability {
    protected final UUID id;
    protected ResourceList cost;
    protected boolean tap;
    protected String text;
    protected transient List<Effect> effects;


    public Ability(Card finalCard, DBAbility dbAbility) {
        this.id = UUID.randomUUID();
        this.cost = new ResourceList(dbAbility.getCost());
        this.tap = dbAbility.isTap();
        this.text = dbAbility.getText();
        this.effects = dbAbility.getEffects().stream()
                .map(dbEffect -> new Effect(finalCard, dbEffect))
                .collect(Collectors.toList());
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

    public void setCost(ResourceList cost) {
        this.cost = cost;
    }

    public boolean isTap() {
        return tap;
    }

    public void setTap(boolean tap) {
        this.tap = tap;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }

    public UUID getId() {
        return id;
    }
}
