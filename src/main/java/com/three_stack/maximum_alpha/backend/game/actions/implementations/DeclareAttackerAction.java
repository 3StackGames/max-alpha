package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.Player;
import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.actions.abstracts.ExistingCardAction;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.prompts.AttackPrompt;
import com.three_stack.maximum_alpha.backend.game.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.events.Event;

import java.util.List;
import java.util.stream.Collectors;

public class DeclareAttackerAction extends ExistingCardAction {

    @Override
    public void run(State state) {
        super.run(state);
        if (!(card instanceof Creature)) {
            throw new IllegalArgumentException("Attacking card isn't a creature");
        }
        Creature attacker = (Creature) card;
        List<Card> attackableTargets = state.getOtherPlayers(player).stream()
                .map(Player::getTargets)
                .flatMap(p -> p.stream())
                .collect(Collectors.toList());

        Prompt attackPrompt = new AttackPrompt(attacker, "Select what you want attacked", attackableTargets);
        state.addPrompt(attackPrompt);

        Event event = new Event(player, " declared " + attacker.getName() + " as attacking");
        state.addEvent(event);
    }
}
