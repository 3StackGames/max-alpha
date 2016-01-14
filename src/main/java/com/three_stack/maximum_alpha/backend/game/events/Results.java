package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class Results {
    public static Result getResult(String resultName) {
        try {
            return (Result) Results.class.getField(resultName).get(Results.class.newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        //@Todo: handle case where there's bad input
        return null;
    }

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
