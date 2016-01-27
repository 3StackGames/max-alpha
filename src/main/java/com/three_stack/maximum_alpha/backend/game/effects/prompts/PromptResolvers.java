package com.three_stack.maximum_alpha.backend.game.effects.prompts;

import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Spell;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.steps.ChooseStep;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.steps.TargetStep;

public class PromptResolvers {
    public static final PromptResolver DEAL_DAMAGE_ALL_TARGET_STEPS = (event, state, prompt) -> {
        Time damageTime = state.getTime();
        prompt.getSteps().stream()
                .filter(step -> step instanceof TargetStep)
                .map(step -> (TargetStep) step)
                .filter(TargetStep::isOptionSelected)
                .forEach( targetStep -> {
                    int damage = (int) targetStep.getValue();
                    prompt.getSource().dealDamage(targetStep.getTarget(), damage, damageTime, state);
                });
    };

    public static final PromptResolver PLAY_CHOSEN_SPELL = (event, state, prompt) -> {
        ChooseStep step = (ChooseStep) prompt.getSteps().get(0);
        Spell chosenSpell = (Spell) step.getChoice();
        chosenSpell.cast(event, state);
    };
}
