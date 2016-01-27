package com.three_stack.maximum_alpha.backend.game.effects.prompts.steps;

public class StepCompleters {
    /**
     * Goes through steps after the current one and removes the input as an option
     */
    public static final StepCompleter EXCLUDE_INPUT_FROM_NEXT_STEPS = (step, input, prompt) -> {
        boolean foundThisStep = false;
        for (Step currentStep : prompt.getSteps()) {
            if(step.equals(currentStep)) {
                foundThisStep = true;
            } else if(foundThisStep && currentStep instanceof OptionContainer) {
                OptionContainer currentOptionContainer = (OptionContainer) currentStep;
                currentOptionContainer.removeOption(input);
            }
        }
    };
}
