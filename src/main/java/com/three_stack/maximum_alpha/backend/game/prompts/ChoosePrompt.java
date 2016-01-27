package com.three_stack.maximum_alpha.backend.game.prompts;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import io.gsonfire.annotations.ExposeMethodResult;

//@Todo: Do this
public abstract class ChoosePrompt extends Prompt {

    public ChoosePrompt(Card source, Player player) {
        super(source, player);
    }

    @ExposeMethodResult("type")
    public String getType() {
        return "ChoosePrompt";
    }
}
