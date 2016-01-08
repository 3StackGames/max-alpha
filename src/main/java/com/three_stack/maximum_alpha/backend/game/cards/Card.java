package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.*;
import com.three_stack.maximum_alpha.backend.game.events.Effect;
import com.three_stack.maximum_alpha.backend.game.events.Trigger;
import com.three_stack.maximum_alpha.backend.game.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//@Todo: Implement initializing controller
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
     * ONLY FOR GSON. DON'T TOUCH.
     */
    private boolean playable;
    private ResourceList.Color dominantColor;

    protected Map<Trigger, List<Effect>> effects;

    protected Card() {
        this.id = UUID.randomUUID();
        name = null;
        defaultCost = null;
        flavorText = null;
    }

    protected Card(String name, ResourceList defaultCost, String text, String flavorText, Map<Trigger, List<Effect>> effects) {
        this.id = UUID.randomUUID();
        this.effects = effects;
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
}
