package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.effects.Result;

//@Todo: most of spell logic still needs to be done. whats done is solely for prompts
public class Spell extends Card {
    protected transient Result result;
    public Spell(String name, ResourceList defaultCost, String text, String flavorText, Result result) {
        super(name, defaultCost, text, flavorText, null);
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
