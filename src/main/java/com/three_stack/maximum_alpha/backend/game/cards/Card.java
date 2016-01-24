package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.*;
import com.three_stack.maximum_alpha.backend.game.events.Effect;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.Outcome;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;
import com.three_stack.maximum_alpha.backend.game.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Card {
	protected final UUID id;
    protected Player controller;
    protected final String name;
    protected final ResourceList defaultCost;
    protected ResourceList currentCost;
    protected String text;
    protected final String flavorText;
    protected List<Counter> counters;
    /**
     * timeEnteredZone is used only when determining effect order. lower values indicate earlier times.
     */
    protected transient int timeEnteredZone;

    /**
     * ONLY FOR GSON. DON'T TOUCH.
     */
    private boolean playable;
    private ResourceList.Color dominantColor;

    protected transient Map<Trigger, List<Effect>> effects;

    protected Card() {
        setup();
        this.id = UUID.randomUUID();
        name = null;
        defaultCost = null;
        flavorText = null;
    }

    protected Card(String name, ResourceList defaultCost, String text, String flavorText, Map<Trigger, List<Effect>> effects) {
        setup();
        this.id = UUID.randomUUID();
        this.effects = effects;
        this.name = name;
        this.defaultCost = defaultCost;
        this.currentCost = defaultCost;
        this.flavorText = flavorText;
        this.dominantColor = calculateDominantColor();
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
    }

    protected void setup() {
        this.timeEnteredZone = 0;
    }

    public Event dealDamage(DamageableCard victim, int amount, State state) {
        List<DamageableCard> victims = new ArrayList<>();
        victims.add(victim);
        return dealDamage(victims, amount, state);
    }

    public Event dealDamage(List<DamageableCard> victims, int amount, State state) {
        if(victims.size() < 1) {
            throw new IllegalArgumentException("Must have at least one victim");
        }
        List<Outcome> damageOutcomes = victims.stream()
                .map(victim -> victim.takeDamage(amount, this))
                .collect(Collectors.toList());
        Event damageEvent = new Event();
        damageEvent.addAllOutcomes(damageOutcomes);
        state.addEvent(damageEvent);
        state.notify(Trigger.ON_DAMAGE, damageEvent);
        return damageEvent;
    }

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

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    public ResourceList.Color getDominantColor() {
        return dominantColor;
    }

    public Player getController() {
        return controller;
    }

    public void setController(Player controller) {
        this.controller = controller;
    }

    public Map<Trigger, List<Effect>> getEffects() {
        return effects;
    }

    public void setEffects(Map<Trigger, List<Effect>> effects) {
        this.effects = effects;
    }

    public boolean hasEffects() {
        return this.effects != null;
    }

    public int getTimeEnteredZone() {
        return timeEnteredZone;
    }

    public void setTimeEnteredZone(int timeEnteredZone) {
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
