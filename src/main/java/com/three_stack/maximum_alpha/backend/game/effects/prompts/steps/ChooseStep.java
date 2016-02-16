package com.three_stack.maximum_alpha.backend.game.effects.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.List;
import java.util.UUID;

public class ChooseStep extends Step implements OptionContainer {
    protected List<Card> choices;
    protected transient Card choice;

    public ChooseStep(String instruction, Object value, List<Card> choices) {
        super(instruction, value, null, null, null, true);
        this.choices = choices;
    }

    public ChooseStep(String instruction, Object value, StepInputChecker stepInputChecker, List<Card> choices) {
        super(instruction, value, stepInputChecker, null, null, true);
        this.choices = choices;
    }

    public ChooseStep(String instruction, Object value, StepCompleter stepCompleter, List<Card> choices) {
        super(instruction, value, null, stepCompleter, null, true);
        this.choices = choices;
    }

    public ChooseStep(String instruction, Object value, StepInputChecker stepInputChecker, StepCompleter stepCompleter, List<Card> choices) {
        super(instruction, value, stepInputChecker, stepCompleter, null, true);
        this.choices = choices;
    }


    public ChooseStep(String instruction, Object value, StepInputChecker stepInputChecker, StepCompleter stepCompleter, List<Card> choices, Step nextStep, boolean isMandatory) {
        super(instruction, value, stepInputChecker, stepCompleter, nextStep, isMandatory);
        this.choices = choices;
    }

    @Override
    public Step complete(Card input, Prompt prompt) {
        setChoice(input);
        return super.complete(input, prompt);
    }

    @Override
    public void reset() {

    }

    @ExposeMethodResult("type")
    public String getType() {
        return "CHOOSE";
    }

    @ExposeMethodResult("choiceId")
    public UUID getChoiceId() {
        if(choice == null) return null;
        return choice.getId();
    }

    public Card getChoice() {
        return choice;
    }

    public void setChoice(Card choice) {
        this.choice = choice;
    }

    public List<Card> getChoices() {
        return choices;
    }

    public void setChoices(List<Card> choices) {
        this.choices = choices;
    }

    @Override
    public boolean isValidOption(Card needle) {
        return choices.contains(needle);
    }

    @Override
    public void removeOption(Card card) {
        choices.remove(card);
    }

    @Override
    public boolean isOptionSelected() {
        return choice != null;
    }
}
