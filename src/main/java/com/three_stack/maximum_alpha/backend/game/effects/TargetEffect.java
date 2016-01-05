package com.three_stack.maximum_alpha.backend.game.effects;

import java.util.List;
import java.util.UUID;

public class TargetEffect extends Effect {
    private List<UUID> targetables;

    @Override
    public String getType() {
        return "TARGET";
    }
}
