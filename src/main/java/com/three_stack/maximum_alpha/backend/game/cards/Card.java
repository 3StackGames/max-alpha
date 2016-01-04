package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.*;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Card {
	protected final long id;
    protected final String name;
    protected final ResourceList defaultCost;
    protected ResourceList currentCost;
    protected String text;
    protected final String flavorText;
    protected List<Counter> counters;

    protected Map<State.TriggerPoint, Trigger> triggers;

    protected Card() {
        id = -1;
        name = null;
        defaultCost = null;
        flavorText = null;
    }

    protected Card(String name, ResourceList defaultCost, String text, String flavorText) {
        id = -1;
        triggers = new HashMap<>();
        this.name = name;
        this.defaultCost = defaultCost;
        this.flavorText = flavorText;
	}

    public void addTrigger(State.TriggerPoint triggerPoint, Trigger trigger) {
        triggers.put(triggerPoint, trigger);
    }

    public ResourceList.Color getDominantColor() {
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

    public long getId() {
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
}
