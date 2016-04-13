package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Tag;

import java.util.List;
import java.util.stream.Collectors;

public class DamagePhase extends Phase {

    public DamagePhase () {
        super();
    }

    
    public void start(State state) {

        List<Creature> attackers = state.getTurnPlayer().getField().getCards().stream()
                .filter(Creature::isAttacking)
                .collect(Collectors.toList());
        Time battleTime = state.getTime();
        Time exhaustTime = state.getTime();
        for(Creature attacker : attackers) {
            if(attacker.isBlocked()) {
                attacker.getBlockers().stream().forEach(blocker -> {
                    if(!attacker.isDead() && !blocker.isDead()) {
                        blocker.block(battleTime, exhaustTime, state);
                    }
                });
            }
            boolean aliveWithPierce = !attacker.isDead() && attacker.hasTag(Tag.TagType.PIERCE);
            if(!attacker.isBlocked() || aliveWithPierce) {
                if(!attacker.getAttackTarget().isDead()) {
                    attacker.attack(battleTime, exhaustTime, state);
                }
            }
            attacker.resetCombat();
        }

        state.resolveDeaths();
        end(state);
    }

    public void end(State state) {
        state.setCombatEnded(true);
        state.setCurrentPhase(new MainPhase());
    }

    @Override
    public String getType() {
        return "DAMAGE_PHASE";
    }
}
