package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Tag;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.utilities.Utility;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BlockPhase extends Phase {
    protected static BlockPhase instance;

    protected BlockPhase() {
        super();
    }

    public static BlockPhase getInstance() {
        if(instance == null) {
            instance = new BlockPhase();
        }
        return instance;
    }

    public void start(State state) {
        state.setCurrentPhase(instance);
        setBlockableCreatures(state);
    }

    public void end(State state) {
        PreparationPhase.getInstance().start(state);
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
        List<Creature> nonAirborneAttackingCreatures = attackingCreatures.stream()
                .filter(creature -> !creature.hasTag(Tag.TagType.AIRBORNE))
                .collect(Collectors.toList());
        state.getPlayersExcept(state.getTurnPlayer()).stream()
                .map(player -> player.getField().getCards())
                .flatMap(Collection::stream)
                .forEach(creature -> {
                    List<Creature> blockableCreatures;
                    if(creature.hasTag(Tag.TagType.AIRBORNE) || creature.hasTag(Tag.TagType.ANTI_AIR)) {
                        blockableCreatures = Utility.copy(attackingCreatures);
                    } else {
                        blockableCreatures = Utility.copy(nonAirborneAttackingCreatures);
                    }
                    creature.setBlockableCreatures(blockableCreatures);
                });
    }
}
