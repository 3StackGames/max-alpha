package com.three_stack.maximum_alpha.backend.game.actions.abstracts;

import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public abstract class CardAction extends Action {
    protected UUID cardId;
    protected Card card;
}
