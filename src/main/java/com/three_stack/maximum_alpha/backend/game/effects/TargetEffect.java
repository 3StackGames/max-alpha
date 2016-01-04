package com.three_stack.maximum_alpha.backend.game.effects;

import java.util.List;

public class TargetEffect extends Effect {
    private List<Long> targetables;

    @Override
    public String getType() {
        return "TARGET";
    }
}
