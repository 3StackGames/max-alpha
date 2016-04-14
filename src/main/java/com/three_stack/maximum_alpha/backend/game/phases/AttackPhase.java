package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.player.Courtyard;
import com.three_stack.maximum_alpha.backend.game.player.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AttackPhase extends Phase {
    public AttackPhase () {
        super();
    }

    public void start(State state) {
        Event startPhaseStartEvent = new Event(state.getTime(), "ATTACK PHASE START");
        state.addEvent(startPhaseStartEvent, Trigger.ON_ATTACK_PHASE_START);
        setAttackableStructures(state);
    }

    public void end(State state) {
        state.setCurrentPhase(BlockPhase.class);
    }

    @Override
    public String getType() {
        return "ATTACK_PHASE";
    }

    private void setAttackableStructures(State state) {
        Player turnPlayer = state.getTurnPlayer();
        turnPlayer.getField().getCards().stream()
                .filter(Creature::canAttack)
                .forEach( creature -> {
                    List<Structure> enemyCastles = state.getPlayersExcept(turnPlayer).stream()
                            .map(Player::getCastle)
                            .filter(Structure::isAttackable)
                            .collect(Collectors.toList());
                    List<Structure> enemyStructures = state.getPlayersExcept(turnPlayer).stream()
                            .map(Player::getCourtyard)
                            .map(Courtyard::getCards)
                            .flatMap(Collection::stream)
                            .filter(Structure::isAttackable)
                            .collect(Collectors.toList());
                    enemyStructures.addAll(enemyCastles);
                    creature.setAttackableStructures(enemyStructures);
                });
    }
}
