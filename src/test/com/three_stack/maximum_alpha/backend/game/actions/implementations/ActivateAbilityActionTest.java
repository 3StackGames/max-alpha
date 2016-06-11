package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Ability;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.effects.Check;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.effects.QueuedEffect;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetStep;
import com.three_stack.maximum_alpha.backend.game.effects.results.implementations.DealDamageResult;
import com.three_stack.maximum_alpha.backend.game.phases.MainPhase;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ActivateAbilityActionTest {
    private State state;
    private Creature creature;
    private Ability ability;
    private List<Player> players;
    private ActivateAbilityAction validAction;

    @Before
    public void setUp() throws Exception {
        createPlayers();
        createState();
        createCreature();
        createAbility(creature);
        createValidAction();
    }

    @Test
    public void testIsValid_ShouldReturnTrue() throws Exception {
        assertTrue(validAction.isValid(state));
    }

    @Test
    public void testRun_ShouldCreateQueuedEffect() {
        validAction.run(state);
        PriorityQueue<QueuedEffect> queuedEffects = state.getQueuedEffects();
        assertEquals(1, queuedEffects.size());
        QueuedEffect queuedEffect = queuedEffects.peek();
        Effect effect = queuedEffect.getEffect();
        assertEquals(ability.getEffects().get(0), effect);
    }

    private void createPlayers() {
        players = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            Player player = new Player(null, 40);
            ResourceList resourceList = new ResourceList(0, 0, 0, 0, 0, 1, 0);
            player.setResources(resourceList);
            players.add(player);
        }
    }

    private void createCreature() {
        creature = new Creature("FooBar", new ResourceList(1), "", "", 1, 1);
        creature.setController(players.get(0));
        creature.setSummoningSickness(false);
        players.get(0).getField().add(creature, Time.getSetup(), state);
        state.generateCardList();
    }

    private void createAbility(Creature creature) {
        List<Check> checks = new ArrayList<>();

        List<Result> results = new ArrayList<>();

        List<TargetStep> targetSteps = new ArrayList<>();

        List<List<String>> includes = new ArrayList<>();

        List<String> enemyCastle = new ArrayList<>();
        enemyCastle.add("CASTLE");
        enemyCastle.add("ENEMY");
        includes.add(enemyCastle);

        List<List<String>> excludes = new ArrayList<>();
        TargetStep targetStep = new TargetStep(false, false, includes, excludes);
        targetSteps.add(targetStep);
        Result result = new DealDamageResult(targetSteps, 1);
        results.add(result);
        Effect effect = new Effect(creature, checks, results);
        List<Effect> effects = new ArrayList<>();
        effects.add(effect);
        ability = new Ability(creature, new ResourceList(1), true, "deal 1 damage to the enemy castle", effects);
        creature.getAbilities().add(ability);
    }

    private void createValidAction() {
        validAction = new ActivateAbilityAction();
        validAction.setPlayerId(players.get(0).getPlayerId());
        validAction.setAbilityId(ability.getId());
        validAction.setCardId(creature.getId());
        validAction.setCost(new ResourceList(0, 0, 0, 0, 0, 1, 0));

        validAction.setup(state);
    }

    private void createState() {
        state = new State();
        state.setCurrentPhase(MainPhase.class);
        state.setPlayers(players);
    }
}
