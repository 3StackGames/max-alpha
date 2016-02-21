package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Castle;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.TargetPrompt;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TargetResult extends Result{
    public class TargetStep extends Step {
        //number of targets the prompt should find
        protected int targetCount;
        protected List<List<String>> includes;
        protected List<List<String>> excludes;
        protected boolean prompt;

        @SuppressWarnings("unchecked")
        public TargetStep(Map<String, Object> map) {
            this.prompt = (boolean) map.get("prompt");
            this.includes = (List<List<String>>) map.get("includes");
            this.excludes = (List<List<String>>) map.get("excludes");
            if(this.prompt) {
                this.targetCount = (int) map.get("targetCount");
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean run(State state, Card source, Event event, Map<String, Object> value) {
            //@Todo: make this actually target properly
            List<NonSpellCard> potentialTargets = state.getPlayingPlayers().stream()
                    .map(Player::getCastle)
                    .collect(Collectors.toList());
            if(prompt) {
                //@Todo: make this based on the database description
                String description = "Select a target";
                Prompt prompt = new TargetPrompt(description, source, source.getController(), event, mandatory, value, potentialTargets);
                state.addPrompt(prompt);
                return true;
            } else {
                List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
                targets.addAll(potentialTargets);
                return false;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public TargetResult(DBResult dbResult) {
        super(dbResult);
        //parse targets to create steps (including prompts)
        List<Map<String, Object>> targetMaps = (List<Map<String, Object>>) dbResult.getValue().get("targets");
        List<TargetStep> targetSteps = targetMaps.stream()
                .map(TargetStep::new)
                .collect(Collectors.toList());
        preparationSteps.addAll(targetSteps);
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value = super.prepareNewValue();
        value.put("targets", new ArrayList<NonSpellCard>());
        return value;
    }
}
