package com.three_stack.maximum_alpha.backend.game.effects.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TargetPrompt extends Prompt {
    protected transient NonSpellCard target;
    protected transient List<NonSpellCard> potentialTargets;

    public TargetPrompt(String description, Card source, Player player, Event event, boolean isMandatory, Map<String, Object> value, List<NonSpellCard> potentialTargets) {
        super(description, source, player, event, isMandatory, value);
        target = null;
        this.potentialTargets = potentialTargets;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resolve(State state) {
        if(target == null) {
            throw new IllegalStateException("target must be set before trying to resolve");
        }
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        targets.add(target);
    }

    @Override
    public boolean isValidInput(Object input)  {
        if(!(input instanceof List)) {
            return false;
        }
        List<NonSpellCard> inputTargets = (List<NonSpellCard>) input;

        return inputTargets.stream()
                .allMatch(inputTarget -> potentialTargets.contains(inputTarget));
    }

    public NonSpellCard getTarget() {
        return target;
    }

    public void setTarget(NonSpellCard target) {
        this.target = target;
    }

    @ExposeMethodResult("parentalTargetIds")
    public List<UUID> getPotentialTargetIds() {
        return potentialTargets.stream()
                .map(NonSpellCard::getId)
                .collect(Collectors.toList());
    }
}
