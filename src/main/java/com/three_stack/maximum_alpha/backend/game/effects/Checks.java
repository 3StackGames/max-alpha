package com.three_stack.maximum_alpha.backend.game.effects;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.SingleCardEvent;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Terminology:
 *  T = Triggerer = a that triggered the result
 *  S = Source = source of the result
 *  C = Controller of Source
 *
 *  How to read checks:
 *  [Subject]_[CONTENT] becomes [Subject] is [Content]
 *
 *  Example 1: S_T becomes "Source is Triggerer"
 *
 *  Example 2: S_ON_FIELD becomes "Source is on field"
 *
 */
public class Checks {
    static Logger log = Logger.getLogger(Checks.class.getName());

    //@Todo: handle checks w/ params
    public static Check getCheck(Map<String, Object> checkMap) {
        String checkName = (String) checkMap.get("name");
        try {
            return (Check) Checks.class.getField(checkName).get(Checks.class.newInstance());
        } catch (Exception e) {
            log.warn("Tried getting check called " + checkName + " but failed. Falling back to FALSE");
            return FALSE;
        }
    }

    public static Check FALSE = (state, effect, event) -> false;

    /**
     * Check if the source and triggerer are the same
     */
    public static Check S_T = (state, effect, event) -> {
        SingleCardEvent singleCardOutcome = (SingleCardEvent) event;
        return effect.getSource().equals(singleCardOutcome.getCard());
    };

    /**
     * Check if the source and triggerer are not the same
     */
    public static Check S_NOT_T = (state, effect, event) -> !S_T.run(state, effect, event);

    /**
     * Check if the triggerer is friendly with the source
     */
    public static Check T_FRIENDLY = (state, effect, event) -> {
        SingleCardEvent singleCardOutcome = (SingleCardEvent) event;
        Player effectController = effect.getSource().getController();
        Player eventController = singleCardOutcome.getCard().getController();
        return effectController.equals(eventController);
    };

    /**
     * Check if the source is on the field
     */
    public static Check S_ON_FIELD = (state, effect, event) -> {
        Card source = effect.getSource();
        return source.getController().getField().getCards().stream().anyMatch(creature -> creature.equals(source));
    };

    public static Check C_TURN_PLAYER = (state, effect, event) -> {
        Card source = effect.getSource();
        Player controller = source.getController();
        return state.getTurnPlayer().equals(controller);
    };
}
