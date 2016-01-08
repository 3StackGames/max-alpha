package com.three_stack.maximum_alpha.backend.game.prompts;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;

//@Todo: Do this
public abstract class ChoosePrompt extends Prompt {

    public ChoosePrompt(Card source, Player player, List<Card> options) {
        super(source, player);
        this.options = options;
    }

    private List<Card> options;
}
