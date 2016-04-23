package com.three_stack.maximum_alpha.backend.game.cards;

import io.gsonfire.annotations.ExposeMethodResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.ResourceList;

public abstract class Card {
	protected final UUID id;
    protected transient Player controller;
    protected final String name;
    protected final ResourceList defaultCost;
    protected ResourceList currentCost;
    protected String text;
    protected final String flavorText;
    protected List<Counter> counters;
    /**
     * timeEnteredZone is used only when determining result order. lower values indicate earlier times.
     */
    protected transient Time timeEnteredZone;

    protected transient Map<Trigger, List<Effect>> triggerEffects;

    protected Card(String name, ResourceList defaultCost, String text, String flavorText) {
        setup();
        this.id = UUID.randomUUID();
        this.name = name;
        this.defaultCost = defaultCost;
        this.currentCost = defaultCost;
        this.text = text;
        this.flavorText = flavorText;
        this.triggerEffects = new HashMap<>();
	}

    protected Card(Card other) {
        setup();
        this.id = UUID.randomUUID();
        this.name = other.name;
        this.defaultCost = other.defaultCost;
        this.currentCost = other.currentCost;
        this.text = other.text;
        this.flavorText = other.flavorText;
        this.counters = other.counters;
        this.triggerEffects = other.triggerEffects.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                            .map(Effect::new).collect(Collectors.toList())
                ));
    }

    protected void setup() {
        this.timeEnteredZone = Time.getSetup();
    }

    public void dealDamage(NonSpellCard victim, int amount, Time time, State state) {
        List<NonSpellCard> victims = new ArrayList<>();
        victims.add(victim);
        dealDamage(victims, amount, time, state);
    }

    public void dealDamage(List<NonSpellCard> victims, int amount, Time time, State state) {
        if(victims.size() < 1) {
            throw new IllegalArgumentException("Must have at least one victim");
        }

        victims.stream()
            .map(victim -> victim.takeDamage(amount, this, time, state))
            .forEach( damageEvent -> state.addEvent(damageEvent, Trigger.ON_DAMAGE));
    }

    public void kill(NonSpellCard victim, Time time, State state) {
        List<NonSpellCard> victims = new ArrayList<>();
        victims.add(victim);
        kill(victims, time, state);
    }

    public void kill(List<NonSpellCard> victims, Time time, State state) {
        if(victims.size() < 1) {
            throw new IllegalArgumentException("Must have at least one victim");
        }

        victims.stream()
                .map(victim -> victim.die(time, state))
                .forEach (deathEvent -> state.addEvent(deathEvent, Trigger.ON_DEATH));
    }
    
    public void heal(NonSpellCard target, int amount, Time time, State state) {
        List<NonSpellCard> targets = new ArrayList<>();
        targets.add(target);
        heal(targets, amount, time, state);
    }

    public void heal(List<NonSpellCard> targets, int amount, Time time, State state) {
        if(targets.size() < 1) {
            throw new IllegalArgumentException("Must have at least one target");
        }

        targets.stream()
                .map(target -> target.receiveHeal(amount, this, time, state))
        		.forEach( healEvent -> state.addEvent(healEvent, Trigger.ON_HEAL));
    }

    @ExposeMethodResult("playable")
    public boolean isPlayable() {
        return true;
    }

    @ExposeMethodResult("dominantColor")
    public ResourceList.Color calculateDominantColor() {
        int max = 0;
        ResourceList.Color dominant = ResourceList.Color.COLORLESS;
        for(ResourceList.Color color : ResourceList.Color.values()) {
            int value = defaultCost.getColor(color);
            if(value > max) {
                max = value;
                dominant = color;
            }
        }
        return dominant;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ResourceList getDefaultCost() {
        return defaultCost;
    }

    public ResourceList getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(ResourceList currentCost) {
        this.currentCost = currentCost;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFlavorText() {
        return flavorText;
    }

    public Player getController() {
        return controller;
    }

    public void setController(Player controller) {
        this.controller = controller;
    }

    public Map<Trigger, List<Effect>> getTriggerEffects() {
        return triggerEffects;
    }

    public void addTriggerEffects(Map<Trigger, List<Effect>> triggerEffects) {
        triggerEffects.entrySet().stream()
                .forEach( entrySet -> {
                    Trigger key = entrySet.getKey();
                    List<Effect> value = entrySet.getValue();
                    if(this.triggerEffects.containsKey(key)) {
                        this.triggerEffects.get(key).addAll(value);
                    } else {
                        this.triggerEffects.put(key, value);
                    }
                });
    }

    public boolean hasEffects() {
        return this.triggerEffects != null;
    }

    public Time getTimeEnteredZone() {
        return timeEnteredZone;
    }

    public void setTimeEnteredZone(Time timeEnteredZone) {
        this.timeEnteredZone = timeEnteredZone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return id != null ? id.equals(card.id) : card.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
