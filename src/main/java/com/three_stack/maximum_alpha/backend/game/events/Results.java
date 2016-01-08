package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class Results {
    public static Result DEAL_DAMAGE_ENEMY_CASTLES  = (state, effect, event, value) -> {
        Card source = effect.getSource();
        Player controller = source.getController();
        int damage = (Integer) value;
        state.getPlayers().stream()
                .filter( player -> !player.equals(controller))
                .forEach( player -> {
                    state.addEvent(player.takeDamage(damage, source));
                });
    };
}
