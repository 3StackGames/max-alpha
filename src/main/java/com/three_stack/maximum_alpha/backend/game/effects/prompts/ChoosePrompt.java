package com.three_stack.maximum_alpha.backend.game.effects.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.List;
import java.util.Map;

public class ChoosePrompt extends Prompt {
    protected transient Card choice;
    protected List<Card> options;


    public ChoosePrompt(String description, Card source, Player player, Event event, boolean isMandatory, Map<String, Object> value, List<Card> options) {
        super(description, source, player, event, isMandatory, value);
        this.options = options;
        this.choice = null;
    }

    @Override
    public void resolve(State state) {
        if(choice == null) {
            throw new IllegalStateException("choice must be set before trying to resolve");
        }
        //set card
        value.put("card", choice);
    }

    @Override
    public boolean isValidInput(Object input) {
        if(!(input instanceof Card)) {
            return false;
        }
        if(!(options.contains(input))) {
            return false;
        }
        return true;
    }

    @ExposeMethodResult("type")
    public String getType() {
        return "CHOOSE";
    }
}
