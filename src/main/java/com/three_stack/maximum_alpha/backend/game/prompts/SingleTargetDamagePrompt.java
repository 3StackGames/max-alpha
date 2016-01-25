package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.DamageableCard;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.TargetStep;

import java.util.List;
import java.util.stream.Collectors;

public class SingleTargetDamagePrompt extends Prompt {
    protected int damage;

    public SingleTargetDamagePrompt(Card source, Player player, boolean isMandatory, int damage, List<DamageableCard> potentialTargets) {
        super(source, player, isMandatory);
        this.damage = damage;
        List<Card> potentialTargetCards = potentialTargets.stream()
                .map(potentialTarget -> (Card) potentialTarget)
                .collect(Collectors.toList());
        steps.add(new TargetStep("Select Target to deal " + damage + " damage.", potentialTargetCards));
    }

    @Override
    public boolean isValidInput(Card input) {
        TargetStep step = (TargetStep) getCurrentStep();
        return step.getTargetables().contains(input);
    }

    @Override
    public void resolve(State state) {
        TargetStep step = (TargetStep) getSteps().get(0);
        source.dealDamage((DamageableCard) step.getTarget(), damage, state.getTime(), state);
    }
}
