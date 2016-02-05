package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.PromptResolvers;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.PromptSpellFactory;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.steps.*;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Results {
    static Logger log = Logger.getLogger(Results.class.getName());

    public static Result getResult(String resultName) {
        try {
            return (Result) Results.class.getField(resultName).get(Results.class.newInstance());
        } catch (Exception e) {
            log.warn("Tried getting result called " + resultName + " but failed. Falling back to DO_NOTHING");
            return DO_NOTHING;
        }
    }

    /**
     * ================ NO INPUT RESULTS================
     */

    /**
     * Called when a result couldn't be properly parsed
     */
    public static Result DO_NOTHING = (state, source, event, value) -> {
    };

    public static Result DEAL_DAMAGE_ALL_STRUCTURES_AND_CASTLES = (state, source, event, value) -> {
        int damage = (int) value;
        List<NonSpellCard> victims = Selectors.structuresAndCastles(state);
        source.dealDamage(victims, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_ALL_CREATURES = (state, source, event, value) -> {
        int damage = (int) value;
        List<NonSpellCard> victims = Selectors.creatures(state);
        source.dealDamage(victims, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_ENEMY_CASTLES = (state, source, event, value) -> {
        Player controller = source.getController();
        int damage = (int) value;
        List<NonSpellCard> castles = Selectors.enemyCastles(controller, state);
        source.dealDamage(castles, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_RANDOM_ENEMY_CREATURE = (state, source, event, value) -> {
        Player controller = source.getController();
        int damage = (int) value;
        Random random = new Random();

        List<Creature> enemyCreatures = Selectors.enemyCreatures(controller, state);

        if (enemyCreatures.isEmpty()) {
            return;
        }

        Creature victim = enemyCreatures.get(random.nextInt(enemyCreatures.size()));
        source.dealDamage(victim, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_FRIENDLY_CASTLE = (state, source, event, value) -> {
        int damage = (int) value;
        source.dealDamage(source.getController().getCastle(), damage, state.getTime(), state);
    };

    /**
     * ================ TARGET RESULTS================
     */

    public static Result TARGET_CREATURE_DEAL_DAMAGE = (state, source, event, value) -> {
        int damage = (int) value;
        List<NonSpellCard> potentialTargets = Selectors.creatures(state);
        if(potentialTargets.size() > 0) {
            String instructions = "Select a creature to deal " + damage + " damage to";
            TargetStep step = new TargetStep(instructions, damage, StepInputCheckers.STEP_INPUT_CHECKER, potentialTargets);
            Prompt prompt = new Prompt(source, source.getController(), event, step, PromptResolvers.DEAL_DAMAGE_ALL_TARGET_STEPS, true);
            state.addPrompt(prompt);
        }
    };

    @SuppressWarnings("unchecked")
    public static Result TARGET_2_CREATURES_DEAL_DAMAGE = (state, source, event, value) -> {
        List<Integer> damages = (List<Integer>) value;
        List<NonSpellCard> potentialTargets = Selectors.creatures(state);
        if(potentialTargets.size() > 1) {
            List<Step> steps = new ArrayList<>();
            for (Integer damage : damages) {
                String instructions = "Select a creature to deal " + damage + " damage to";
                TargetStep step = new TargetStep(instructions, damage, StepInputCheckers.STEP_INPUT_CHECKER, StepCompleters.EXCLUDE_INPUT_FROM_NEXT_STEPS, potentialTargets);
                steps.add(step);
            }

            Prompt prompt = new Prompt(source, source.getController(), event, steps, PromptResolvers.DEAL_DAMAGE_ALL_TARGET_STEPS, true);
            state.addPrompt(prompt);
        }
    };

    /**
     * ================ CHOOSE RESULTS================
     */

    public static Result CHOOSE_DEAL_DAMAGE_ENEMY_CASTLES_OR_DEAL_DAMAGE_ALL_CREATURES = (state, source, event, value) -> {
        int damage = (int) value;
        List<Card> choices = new ArrayList<>();
        choices.add(PromptSpellFactory.createDamageAllEnemyCastles(source, damage));
        choices.add(PromptSpellFactory.createDamageAllCreatures(source, damage));
        ChooseStep chooseStep = new ChooseStep("Choose a result", value, StepInputCheckers.STEP_INPUT_CHECKER, choices);
        Prompt prompt = new Prompt(source, source.getController(), event, chooseStep, PromptResolvers.PLAY_CHOSEN_SPELL, true);
        state.addPrompt(prompt);
    };
}
