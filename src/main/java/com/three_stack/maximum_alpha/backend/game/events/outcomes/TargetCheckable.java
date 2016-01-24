package com.three_stack.maximum_alpha.backend.game.events.outcomes;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public interface TargetCheckable {
    public boolean isTarget(Card needle);
}
