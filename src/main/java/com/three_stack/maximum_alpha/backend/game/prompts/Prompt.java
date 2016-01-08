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

    public Prompt(Card source, Player player) {
        this.source = source;
        this.steps = new ArrayList<>();
        this.player = player;
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
}
