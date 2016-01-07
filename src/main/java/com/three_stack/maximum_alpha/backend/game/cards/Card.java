package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.*;
import com.three_stack.maximum_alpha.backend.game.Trigger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Card {
	protected final UUID id;
    protected final String name;
    protected final ResourceList defaultCost;
    protected ResourceList currentCost;
    protected String text;
    protected final String flavorText;
    protected List<Counter> counters;

    /**
     * ONLY FOR GSON. DON'T TOUCH.
     */
    private boolean playable;
    private ResourceList.Color dominantColor;

    protected Map<State.TriggerPoint, Trigger> triggers;

    protected Card() {
        this.id = UUID.randomUUID();
        name = null;
        defaultCost = null;
        flavorText = null;
    }

    protected Card(String name, ResourceList defaultCost, String text, String flavorText) {
        this.id = UUID.randomUUID();
        triggers = new HashMap<>();
        this.name = name;
        this.defaultCost = defaultCost;
        this.currentCost = defaultCost;
        this.flavorText = flavorText;
        this.dominantColor = calculateDominantColor();
	}

    protected Card(Card other) {
        this.id = UUID.randomUUID();
        this.name = other.name;
        this.defaultCost = other.defaultCost;
        this.currentCost = other.currentCost;
        this.text = other.text;
        this.flavorText = other.flavorText;
        this.counters = other.counters;
    }

    public void addTrigger(State.TriggerPoint triggerPoint, Trigger trigger) {
        triggers.put(triggerPoint, trigger);
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
}
