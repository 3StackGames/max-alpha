package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.Event;

import java.util.List;
import java.util.UUID;

public class AttackEffect extends SingleTargetEffect {

    public AttackEffect(Card source, String prompt, List<Card> targetables) {
        super(source, prompt, targetables);
    }

    @Override
    public void resolve(State state, Card target) {
        Creature attacker = (Creature) getSource();
        attacker.setAttackTarget(target);

        Event event = new Event(attacker.getName() + " is now attacking " + target.getName());
        state.addEvent(event);
    }
}
