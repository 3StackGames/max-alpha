package com.three_stack.maximum_alpha.backend.game.effects.prompts;

import io.gsonfire.annotations.ExposeMethodResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.steps.Step;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class Prompt {
    protected transient Card source;
    protected Stack<Step> steps;
    protected Step currentStep;
    protected transient Player player;
    protected boolean isMandatory;
    protected transient PromptResolver promptResolver;
    /**
     * event is the event that caused the prompt. For example, an event triggered by ON_PLAY.
     */
    protected Event event;

    public Prompt(Card source, Player player, Event event, Step step, PromptResolver promptResolver, boolean isMandatory) {
        setup(source, player, event, step, promptResolver, isMandatory);
    }
    
    public Prompt(Card source, Player player, Event event, List<Step> steps, PromptResolver promptResolver, boolean isMandatory) {
    	Step step = null;
    	if(!steps.isEmpty()) {
    		step = steps.get(0);
	    	for(int i = 0; i < steps.size()-1; i++) {
	    		steps.get(i).setNextStep(steps.get(i+1));
	    	}
    	}
        setup(source, player, event, step, promptResolver, isMandatory);
    }

    protected void setup(Card source, Player player, Event event, Step step, PromptResolver promptResolver, boolean isMandatory) {
        this.source = source;
        this.event = event;
        this.player = player;
        this.currentStep = step;
        this.promptResolver = promptResolver;
        this.isMandatory = isMandatory;
        
        this.steps = new Stack<>();
    }

    public void completeCurrentStep(Card input) {
    	steps.push(currentStep);
        currentStep = getCurrentStep().complete(input, this);
    }

    public boolean isValidInput(Card input) {
        return getCurrentStep().isValidInput(input, this);
    }

    public void resolve(State state) {
        promptResolver.run(event, state, this);
    }

    public boolean isDone() {
        return currentStep == null;
    }

    public Card getSource() {
        return source;
    }

    public Step getCurrentStep() {
        return currentStep;
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
    
    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean canUndo() {
        if (isMandatory() && steps.size() == 0)
            return false;
        return true;
    }

    /**
     * Undoes the current step.
     *
     * @return True if the prompt should be removed from the queue, false otherwise.
     */
    public boolean undo() {
        if (steps.size() > 0) {
        	currentStep = steps.pop();
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
