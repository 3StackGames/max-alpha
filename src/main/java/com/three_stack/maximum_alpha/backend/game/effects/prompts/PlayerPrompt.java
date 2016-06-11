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

public class PlayerPrompt extends Prompt {
    protected transient Player target;
    protected transient List<Player> potentialPlayers;

    public PlayerPrompt(String description, Card source, Player player, Event event, boolean isMandatory, Map<String, Object> value, List<Player> potentialPlayers) {
        super(description, source, player, event, isMandatory, value);
        target = null;
        this.potentialPlayers = potentialPlayers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resolve(State state) {
        if(target == null) {
            throw new IllegalStateException("player must be set before trying to resolve");
        }
        List<Player> targets = (List<Player>) value.get("players");
        targets.add(target);
    }

    @Override
    public boolean isValidInput(Object input)  {
        if(!(input instanceof Player)) {
            return false;
        }
        if(!potentialPlayers.contains(input)) {
            return false;
        }
        return true;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    @ExposeMethodResult("targetableIds")
    public List<UUID> getPotentialTargetIds() {
        return potentialPlayers.stream()
                .map(Player::getPlayerId)
                .collect(Collectors.toList());
    }

    @ExposeMethodResult("type")
    public String getType() {
        return "PLAYER";
    }
}
