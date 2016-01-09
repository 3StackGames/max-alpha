package com.three_stack.maximum_alpha.backend.game.prompts;

import java.util.ArrayList;
import java.util.List;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.Step;

public abstract class Prompt {
    protected final Card source;
    protected List<Step> steps;
    protected int currentStep;
	protected final Player player;
	protected boolean isMandatory;

    protected Prompt(Card source, Player player) {
        this.source = source;
        this.steps = new ArrayList<>();
        this.player = player;
        isMandatory = false;
        currentStep = 0;
    }
    
    protected Prompt(Card source, Player player, boolean isMandatory) {
        this.source = source;
        this.steps = new ArrayList<>();
        this.player = player;
        this.isMandatory = isMandatory;
        currentStep = 0;
    }

	public void processCurrentStep(Card input) {
        currentStep++;
    }
    
    public abstract boolean isValidInput(Card input);

    public abstract void resolve(State state);

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
	
	public boolean canUndo() {
		if(isMandatory() && currentStep == 0)
			return false;
		return true;
	}
	
	/**
	 * Undoes the current step.
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
}
