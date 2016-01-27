package com.three_stack.maximum_alpha.backend.game.prompts;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import io.gsonfire.annotations.ExposeMethodResult;

//@Todo: Do this
public abstract class ChoosePrompt extends Prompt {
    protected List<Card> options;

    public ChoosePrompt(Card source, Player player, List<Card> options) {
        super(source, player);
        this.options = options;
    }

    public List<Card> getOptions() {
        return options;
    }

    public void setOptions(List<Card> options) {
        this.options = options;
    }

    @ExposeMethodResult("type")
    public String getType() {
        return "ChoosePrompt";
    }
}
