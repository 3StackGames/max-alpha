package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class MultiTargetEffect extends Effect {
    private List<UUID> targetables;

    public abstract void resolve(State state, List<Card> targets);
}
