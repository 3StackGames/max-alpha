package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BlockPhase extends Phase {
    public BlockPhase() {
        super();
    }


    public void start(State state) {
        setBlockableCreatures(state);
    }

    public void end(State state) {
        state.setCurrentPhase(PreparationPhase.class);
        Event startPhaseStartEvent = new Event(state.getTime(), "BLOCK PHASE END");
        state.addEvent(startPhaseStartEvent, Trigger.ON_BLOCK_PHASE_END);
    }

    @Override
    public String getType() {
        return "BLOCK_PHASE";
    }

    private void setBlockableCreatures(State state) {
        List<Creature> attackingCreatures = state.getTurnPlayer().getField().getCards().stream()
                .filter(Creature::isAttacking)
                .collect(Collectors.toList());
        state.getPlayersExcept(state.getTurnPlayer()).stream()
                .map(player -> player.getField().getCards())
                .flatMap(Collection::stream)
                .forEach(creature -> creature.determineBlockableAttackers(attackingCreatures));
    }
}
