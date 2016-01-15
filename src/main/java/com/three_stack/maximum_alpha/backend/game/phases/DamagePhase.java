package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DamagePhase extends Phase {
    protected static DamagePhase instance;

    protected DamagePhase () {
        super("Damage Phase");
    }

    public static DamagePhase getInstance() {
        if(instance == null) {
            instance = new DamagePhase();
        }
        return instance;
    }
    
    public void start(State state) {
        state.setCurrentPhase(instance);

        List<Creature> attackers = state.getTurnPlayer().getField().getCreatures().stream()
                .filter(Creature::isAttacking)
                .collect(Collectors.toList());

        for(Creature attacker : attackers) {
            if(!attacker.isBlocked()) {
                state.addEvent(attacker.getAttackTarget().takeDamage(attacker.getCurrentAttack(), attacker));
            } else {
                attacker.getBlockers().stream().forEach(blocker -> {
                	if(!attacker.isDead() && !blocker.isDead()) {
	                    state.addEvent(attacker.takeDamage(blocker.getCurrentAttack(), blocker));
	                    state.addEvent(blocker.takeDamage(attacker.getCurrentAttack(), attacker));
	                    state.resolveDeaths();
                	}
	
	                blocker.setBlockTarget(null);
                });
                attacker.resetBlockers();
            }
            attacker.setAttackTarget(null);
            attacker.setExhausted(true);
        }

        end(state);
    }

    public void end(State state) {
        state.setCombatEnded(true);
        MainPhase.getInstance().start(state);
    }
}
