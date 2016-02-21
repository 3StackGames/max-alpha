package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;

import java.util.Map;

public abstract class Step implements Comparable<Step> {
    protected int order;
    protected boolean mandatory;

    public Step() {
        /**
         * If order isn't specified it'll go last
         */
        order = Integer.MAX_VALUE;
        mandatory = true;
    }

    public Step(boolean mandatory) {
        this.mandatory = mandatory;
        this.order = Integer.MAX_VALUE;
    }

    public Step(boolean mandatory, int order) {
        this.order = order;
        this.mandatory = mandatory;
    }

    /**
     *
     * @param state
     * @param source
     * @param event
     * @param value
     * @return whether a prompt has been created
     */
    public abstract boolean run(State state, Card source, Event event, Map<String, Object> value);

    @Override
    public int compareTo(Step o) {
        return this.order - o.order;
    }

    /**
     * Auto-Generated Getters and Setters
     */

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
}
