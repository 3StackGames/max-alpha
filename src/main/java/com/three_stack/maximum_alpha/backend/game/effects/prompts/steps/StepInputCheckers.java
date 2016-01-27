package com.three_stack.maximum_alpha.backend.game.effects.prompts.steps;

public class StepInputCheckers {
    public static final StepInputChecker STEP_INPUT_CHECKER = (step, input, prompt) -> {
        if(input == null) return false;
        OptionContainer targetStep = (OptionContainer) step;
        return targetStep.isValidOption(input);
    };
}
