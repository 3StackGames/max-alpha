package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.CardFactory;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.ChoosePrompt;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.utilities.DatabaseClientFactory;
import com.three_stack.maximum_alpha.database_client.DatabaseClient;
import com.three_stack.maximum_alpha.database_client.pojos.DBCard;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ChooseResult extends Result {
    public class ChooseStep extends Step {
        //use DBCard so we can easily initialize new cards
        //@Todo: consider just instantiating instead of keeping a template
        protected List<DBCard> optionTemplates;

        @SuppressWarnings("unchecked")
        public ChooseStep(Map<String, Object> map) {
            List<ObjectId> optionIds = (List<ObjectId>) map.get("options");
            this.optionTemplates = optionIds.stream()
                    .map(DatabaseClientFactory::getCard)
                    .collect(Collectors.toList());
        }
        protected ChooseStep() {
            //used for deep copying only
        }

        public ChooseStep(Step other) {
            super(other);
            ChooseStep otherChooseStep = (ChooseStep) other;
            this.optionTemplates = otherChooseStep.optionTemplates.stream().collect(Collectors.toList());
        }

        @Override
        public boolean run(State state, Card source, Event event, Map<String, Object> value) {
            //@Todo: make this based on the database description
            String description = "Select an option";
            List<Card> options = optionTemplates.stream()
                    .map(optionTemplates -> {
                        Card card = CardFactory.create(optionTemplates);
                        card.setController(source.getController());
                        return card;
                    })
                    .collect(Collectors.toList());
            Prompt prompt = new ChoosePrompt(description, source, source.getController(), event, mandatory, value, options);
            state.addPrompt(prompt);
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public ChooseResult(DBResult dbResult) {
        super(dbResult);
        if(!dbResult.getValue().containsKey("card")) {
            throw new IllegalArgumentException("choose result must have options");
        }
        ChooseStep chooseStep = new ChooseStep((Map<String, Object>) dbResult.getValue().get("card"));
        getPreparationSteps().add(chooseStep);
    }

    public ChooseResult(Result other) {
        super(other);
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value = super.prepareNewValue();
        //do nothing right now
        return value;
    }
}
