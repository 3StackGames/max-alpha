package com.three_stack.maximum_alpha.backend.game.events;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.events.outcomes.SingleCardOutcome;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import org.apache.log4j.Logger;

/**
 * Terminology:
 *  T = Triggerer = a that triggered the effect
 *  S = Source = source of the effect
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

    public static Check getCheck(String checkName) {
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
        SingleCardOutcome singleCardOutcome = (SingleCardOutcome) event;
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
        SingleCardOutcome singleCardOutcome = (SingleCardOutcome) event;
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
}
