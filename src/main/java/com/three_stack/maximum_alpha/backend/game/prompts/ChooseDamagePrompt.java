package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.ChooseStep;

import java.util.List;
import java.util.stream.Collectors;

public class ChooseDamagePrompt extends ChoosePrompt {
    protected int damage;
    public ChooseDamagePrompt(Card source, Player player, List<Card> options, int damage) {
        super(source, player, options);
        steps.add(new ChooseStep("Choose an effect"));
        this.damage = damage;
    }

    @Override
    public boolean isValidInput(Card input) {
        if(input == null) return false;
        return options.contains(input);
    }

    @Override
    public void resolve(State state) {
        Card choice = ((ChooseStep) steps.get(0)).getChoice();
        if(choice.getName().equals("Deal Damage Castles")) {
            List<NonSpellCard> victims = state.getPlayingPlayers().stream()
                    .map(player -> player.getCastle())
                    .collect(Collectors.toList());
            choice.dealDamage(victims,damage,state.getTime(),state);
        } else {
            List<NonSpellCard> victims = state.getPlayingPlayers().stream()
                    .map(player -> player.getField().getCards())
                    .flatMap(cards -> cards.stream())
                    .collect(Collectors.toList());
            choice.dealDamage(victims, damage, state.getTime(), state);
        }
    }
}
