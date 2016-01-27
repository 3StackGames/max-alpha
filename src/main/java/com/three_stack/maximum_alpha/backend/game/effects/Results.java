package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.cards.Spell;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.player.Zone;
import com.three_stack.maximum_alpha.backend.game.prompts.InputChecker;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.prompts.Resolver;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.ChooseStep;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.Step;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.TargetStep;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * Called when a result couldn't be properly parsed
     */
    public static Result DO_NOTHING = (state, source, event, value) -> {

    };

    /**
     * NO INPUT
     */

    public static Result DEAL_DAMAGE_ALL_STRUCTURES_AND_CASTLES = (state, source, event, value) -> {
        int damage = (int) value;

        Stream<NonSpellCard> castleStream = state.getPlayingPlayers().stream()
                .map(Player::getCastle);

        Stream<NonSpellCard> structureStream = state.getPlayingPlayers().stream()
                .map(player -> player.getCourtyard().getCards())
                .flatMap(Collection::stream);

        List<NonSpellCard> victims = Stream.concat(castleStream, structureStream).collect(Collectors.toList());
        source.dealDamage(victims, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_ALL_CREATURES = (state, source, event, value) -> {
        int damage = (int) value;
        List<NonSpellCard> victims = state.getPlayingPlayers().stream()
                .map(player -> player.getField().getCards())
                .flatMap(creatures -> creatures.stream())
                .collect(Collectors.toList());
        source.dealDamage(victims, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_ENEMY_CASTLES = (state, source, event, value) -> {
        Player controller = source.getController();
        int damage = (int) value;
        List<NonSpellCard> castles = state.getPlayersExcept(controller).stream()
                .map(Player::getCastle)
                .collect(Collectors.toList());
        source.dealDamage(castles, damage, state.getTime(), state);
    };

    public static Result DEAL_DAMAGE_RANDOM_ENEMY_CREATURE = (state, source, event, value) -> {
        Player controller = source.getController();
        int damage = (int) value;
        Random random = new Random();

        List<Creature> enemyCreatures = state.getPlayersExcept(controller).stream()
                .map(player -> player.getField().getCards())
                .flatMap(creatures -> creatures.stream())
                .collect(Collectors.toList());

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

    protected static final InputChecker inCurrentTargetables = (input, prompt) -> {
        if(input == null) return false;
        TargetStep targetStep = (TargetStep) prompt.getCurrentStep();
        return targetStep.getTargetables().contains(input);
    };

    protected static final Resolver dealTargetDamage = (event, state, prompt) -> {
        TargetStep step = (TargetStep) prompt.getSteps().get(0);
        int damage = (int) step.getValue();
        prompt.getSource().dealDamage(step.getTarget(), damage, state.getTime(), state);
    };

    /**
     * TARGET
     */

    public static Result TARGET_CREATURE_DEAL_DAMAGE = (state, source, event, value) -> {
        int damage = (int) value;
        List<NonSpellCard> potentialTargets = state.getAllPlayers().stream()
                .map(Player::getField)
                .map(Zone::getCards)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if(potentialTargets.size() > 0) {
            List<Step> steps = new ArrayList<>();
            TargetStep step = new TargetStep("Select a creature to deal " + damage + " damage to", damage, potentialTargets);
            steps.add(step);
            Prompt prompt = new Prompt(source, source.getController(), event, steps, inCurrentTargetables, dealTargetDamage, true);
            state.addPrompt(prompt);
        }
    };

    protected static final InputChecker inCurrentChoices = (input, prompt) -> {
        if(input == null) return false;
        ChooseStep chooseStep = (ChooseStep) prompt.getCurrentStep();
        return chooseStep.getChoices().contains(input);
    };

    protected static final Resolver playChosenSpell = (event, state, prompt) -> {
        ChooseStep step = (ChooseStep) prompt.getSteps().get(0);
        Spell chosenSpell = (Spell) step.getChoice();
        chosenSpell.cast(event, state);
    };

    protected static Spell getDamageAllEnemyCastles(Card source, int damage) {
        Spell damageEnemyCastles = new Spell("Damage All Enemy Castles", new ResourceList(), "Deal " + damage + " damage to all enemy castles", "");

        Effect effect = new Effect(source);
        effect.addResult(Results.DEAL_DAMAGE_ENEMY_CASTLES, damage);
        damageEnemyCastles.addEffect(effect);

        return damageEnemyCastles;
    }
    
    protected static Spell getDamageAllCreatures(Card source, int damage) {
        Spell damageAllCreatures = new Spell("Damage All Creatures", new ResourceList(), "Deal " + damage + " damage to all creatures", "");
        
        Effect effect = new Effect(source);
        effect.addResult(Results.DEAL_DAMAGE_ALL_CREATURES, damage);
        damageAllCreatures.addEffect(effect);

        return damageAllCreatures;
    }

    /**
     * CHOOSE
     */

    public static Result CHOOSE_DEAL_DAMAGE_ENEMY_CASTLES_OR_DEAL_DAMAGE_ALL_CREATURES = (state, source, event, value) -> {
        int damage = (int) value;
        List<Card> choices = new ArrayList<>();
        choices.add(getDamageAllEnemyCastles(source, damage));
        choices.add(getDamageAllCreatures(source, damage));
        List<Step> steps = new ArrayList<>();
        ChooseStep chooseStep = new ChooseStep("Choose a result", value, choices);
        steps.add(chooseStep);
        Prompt prompt = new Prompt(source, source.getController(), event, steps, inCurrentChoices, playChosenSpell);
        state.addPrompt(prompt);
    };
}
