package com.three_stack.maximum_alpha.backend.game.effects;

import java.util.List;

public class ChooseEffect extends Effect {
    private List<String> options;

    @Override
    public String getType() {
        return "CHOOSE";
    }
}
