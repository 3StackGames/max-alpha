package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.*;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.player.Player;

import java.util.*;

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

    /**
     * ONLY FOR GSON. DON'T TOUCH.
     */
    private boolean playable;
    private ResourceList.Color dominantColor;

    protected transient Map<Trigger, List<Effect>> triggerEffects;

    protected Card(String name, ResourceList defaultCost, String text, String flavorText) {
        setup();
        this.id = UUID.randomUUID();
        this.name = name;
        this.defaultCost = defaultCost;
        this.currentCost = defaultCost;
        this.text = text;
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
            .map(victim -> victim.takeDamage(amount, this, time))
            .forEach( damageEvent -> state.addEvent(damageEvent, Trigger.ON_DAMAGE));
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

    public Map<Trigger, List<Effect>> getTriggerEffects() {
        return triggerEffects;
    }

    public void setTriggerEffects(Map<Trigger, List<Effect>> triggerEffects) {
        this.triggerEffects = triggerEffects;
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
