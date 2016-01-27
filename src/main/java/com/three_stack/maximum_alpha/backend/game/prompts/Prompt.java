package com.three_stack.maximum_alpha.backend.game.prompts;

import io.gsonfire.annotations.ExposeMethodResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.backend.game.prompts.steps.Step;

public abstract class Prompt {
    protected final transient Card source;
    protected List<Step> steps;
    protected int currentStep;
    protected transient final Player player;
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

    public void completeCurrentStep(Card input) {
        getCurrentStep().complete(input);
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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
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
