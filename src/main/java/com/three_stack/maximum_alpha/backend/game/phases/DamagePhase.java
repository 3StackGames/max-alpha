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


        List<Creature> blockers = state.getOtherPlayers(state.getTurnPlayer()).stream()
                .map( player -> player.getField().getCards())
                .flatMap( cards -> cards.stream())
                .filter( Creature::isBlocking)
                .collect(Collectors.toList());

        Set<Creature> blockedAttackers = blockers.stream()
                .map( blocker -> blocker.getBlockTarget())
                .collect(Collectors.toSet());

        //handle blocks
        //@Todo: handle blocking order
        for(Creature blocker : blockers) {
            Creature attacker = blocker.getBlockTarget();
            if(attacker.isAlive()) {
                state.addEvent(attacker.takeDamage(blocker.getCurrentAttack(), blocker));
                state.addEvent(blocker.takeDamage(attacker.getCurrentAttack(), attacker));

                blocker.setBlockTarget(null);
            }
        }

        //handle attacks to structures
        List<Creature> attackers = state.getTurnPlayer().getField().getCards().stream()
                .filter( Creature::isAttacking)
                .collect(Collectors.toList());

        attackers.forEach((attacker) -> {
            if(!blockedAttackers.contains(attacker)) {
                state.addEvent(attacker.getAttackTarget().takeDamage(attacker.getCurrentAttack(), attacker));
            }
            attacker.setAttackTarget(null);
            attacker.setExhausted(true);
        });

        end(state);
    }

    public void end(State state) {
        state.setCombatEnded(true);
        MainPhase.getInstance().start(state);
    }
}
