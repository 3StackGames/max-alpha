package com.three_stack.maximum_alpha.backend.game.phases;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;

public class EndPhase extends Phase {
    protected static EndPhase instance;

    protected EndPhase() {
        super();
    }

    public static EndPhase getInstance() {
        if(instance == null) {
            instance = new EndPhase();
        }
        return instance;
    }

    public void start(State state) {
        state.setCurrentPhase(instance);
        state.getPlayingPlayers().stream()
                .forEach(player -> {
                    player.getField().getCards().stream()
                            .filter(Creature::isSummonedThisTurn)
                            .forEach(creature -> {
                                creature.setSummonedThisTurn(false);
                                creature.setSummoningSickness(false);
                            });
                });
        end(state);
    }

    public void end(State state) {
    	//TODO: Hand size discard
        state.newTurn();
        StartPhase.getInstance().start(state);
        Event startPhaseStartEvent = new Event(state.getTime(), "END PHASE END");
        state.addEvent(startPhaseStartEvent, Trigger.ON_END_PHASE_END);
    }

    @Override
    public String getType() {
        return "END_PHASE";
    }
}
