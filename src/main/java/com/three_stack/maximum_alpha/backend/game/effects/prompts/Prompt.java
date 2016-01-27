package com.three_stack.maximum_alpha.backend.game.effects.prompts;

import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.steps.Step;

public class Prompt {
    protected transient Card source;
    protected List<Step> steps;
    protected int currentStep;
    protected transient Player player;
    protected boolean isMandatory;
    protected transient PromptResolver promptResolver;
    /**
     * event is the event that caused the prompt. For example, an event triggered by ON_PLAY.
     */
    protected Event event;

    public Prompt(Card source, Player player, Event event, List<Step> steps, PromptResolver promptResolver) {
        setup(source, player, event, steps, promptResolver);
    }

    public Prompt(Card source, Player player, Event event, List<Step> steps, PromptResolver promptResolver, boolean isMandatory) {
        setup(source, player, event, steps, promptResolver);
        this.isMandatory = isMandatory;
    }

    protected void setup(Card source, Player player, Event event, List<Step> steps, PromptResolver promptResolver) {
        this.source = source;
        this.steps = new ArrayList<>();
        this.player = player;
        this.event = event;
        this.steps = steps;
        this.promptResolver = promptResolver;

        this.currentStep = 0;
        this.isMandatory = false;

    }

    public void completeCurrentStep(Card input) {
        getCurrentStep().complete(input, this);
        currentStep++;
    }

    public boolean isValidInput(Card input) {
        return getCurrentStep().isValidInput(input, this);
    }

    public void resolve(State state) {
        promptResolver.run(event, state, this);
    }

    public boolean isDone() {
        return currentStep >= steps.size();
    }

    public Card getSource() {
        return source;
    }

    public Step getCurrentStep() {
        return steps.get(currentStep);
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean canUndo() {
        if (isMandatory() && currentStep == 0)
            return false;
        return true;
    }

    /**
     * Undoes the current step.
     *
     * @return True if the prompt should be removed from the queue, false otherwise.
     */
    public boolean undo() {
        if (currentStep > 0) {
            currentStep--;
            getCurrentStep().reset();
            return false;
        } else {
            return true;
        }
    }

    @ExposeMethodResult("playerId")
    public UUID getPlayerId() {
        return player.getPlayerId();
    }

    @ExposeMethodResult("sourceId")
    public UUID getSourceId() {
        return source.getId();
    }
}
