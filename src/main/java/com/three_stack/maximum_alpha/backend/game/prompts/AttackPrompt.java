package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.cards.Structure;
import com.three_stack.maximum_alpha.backend.game.events.Event;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.TargetStep;

import java.util.List;

public class AttackPrompt extends TargetStep {

    public AttackPrompt(Card source, String prompt, List<Card> targetables) {
        super(source, prompt, targetables);
    }

    @Override
    public void resolve(State state, Card target) {
        Creature attacker = (Creature) getSource();
        attacker.setAttackTarget((Structure) target);

        Event event = new Event(attacker.getName() + " is now attacking " + target.getName());
        state.addEvent(event);
    }
}
