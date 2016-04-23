package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;

import java.util.Map;

public abstract class Step implements Comparable<Step> {
    protected int order;
    protected boolean mandatory;
    protected Result result;
    protected boolean prompt;

    public Step(Result result, boolean prompt) {
        this.result = result;
        this.prompt = prompt;
        /**
         * If order isn't specified it'll go last
         */
        order = Integer.MAX_VALUE;
        mandatory = true;
    }

    public Step(Result result, boolean prompt, boolean mandatory) {
        this.prompt = prompt;
        this.result = result;
        this.mandatory = mandatory;
        this.order = Integer.MAX_VALUE;
    }

    public Step(Result result, boolean prompt, boolean mandatory, int order) {
        this.result = result;
        this.prompt = prompt;
        this.order = order;
        this.mandatory = mandatory;
    }

    public Step(Step other) {
        this.result = other.result;
        this.prompt = other.prompt;
        this.order = other.order;
        this.mandatory = other.mandatory;
    }

    /**
     *
     * @param state
     * @param source
     * @param event
     * @param value
     * @return whether a prompt has been created
     */
    public abstract void run(State state, Card source, Event event, Map<String, Object> value);

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

    public Result getResult() {
        return result;
    }

    public void setPrompt(boolean prompt) {
        this.prompt = prompt;
    }

    public boolean isPrompt() {
        return prompt;
    }
}
