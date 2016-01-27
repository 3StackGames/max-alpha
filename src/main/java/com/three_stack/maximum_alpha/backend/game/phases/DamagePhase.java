package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;

import java.util.List;
import java.util.stream.Collectors;

public class DamagePhase extends Phase {
    protected static DamagePhase instance;

    protected DamagePhase () {
        super();
    }

    public static DamagePhase getInstance() {
        if(instance == null) {
            instance = new DamagePhase();
        }
        return instance;
    }
    
    public void start(State state) {
        state.setCurrentPhase(instance);

        List<Creature> attackers = state.getTurnPlayer().getField().getCards().stream()
                .filter(Creature::isAttacking)
                .collect(Collectors.toList());
        Time battleTime = state.getTime();
        Time exhaustTime = state.getTime();
        for(Creature attacker : attackers) {
            if(!attacker.isBlocked()) {
            	if(!attacker.getAttackTarget().isDead()) {
	                attacker.attack(battleTime, exhaustTime, state);
            	}
            } else {
                attacker.getBlockers().stream().forEach(blocker -> {
                	if(!attacker.isDead() && !blocker.isDead()) {
                        blocker.block(battleTime, exhaustTime, state);
                    }
                });
                attacker.resetBlockers();
            }
        }

        state.resolveDeaths();
        end(state);
    }

    public void end(State state) {
        state.setCombatEnded(true);
        MainPhase.getInstance().start(state);
    }

    @Override
    public String getType() {
        return "DAMAGE_PHASE";
    }
}
