package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.player.ResourceList;

import java.util.ArrayList;
import java.util.List;

public class Spell extends Card {
    protected transient List<Effect> effects;

    public Spell(String name, ResourceList defaultCost, String text, String flavorText) {
        super(name, defaultCost, text, flavorText);
        this.effects = new ArrayList<>();
    }

    public void addEffect(Effect effect) {
        getEffects().add(effect);
    }

    public void cast(Event event, State state) {
        getEffects().stream()
                .filter(effect -> effect.getChecks().stream().allMatch(check -> check.run(state, effect, event)))
                .forEach(effect -> effect.trigger(event, state));
    }
    /**
     * AUTO-GENERATED GETTERS AND SETTERS
     *
     */

    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }
}
