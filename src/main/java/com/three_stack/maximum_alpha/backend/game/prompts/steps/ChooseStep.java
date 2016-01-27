package com.three_stack.maximum_alpha.backend.game.prompts.steps;

import com.three_stack.maximum_alpha.backend.game.cards.Card;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChooseStep extends Step {
    protected List<Card> choices;
    protected transient Card choice;

    public ChooseStep(String instruction, Object value, List<Card> choices) {
        super(instruction, value);
        this.choices = choices;
    }

    @Override
    public void complete(Card input) {
        setChoice(input);
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
}
