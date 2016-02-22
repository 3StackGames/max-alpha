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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class TargetResult extends Result{
    protected static final List<String> filterBases = Arrays.asList("CREATURE", "STRUCTURE", "CASTLE");
    public class TargetStep extends Step {

        //number of targets the prompt should find
        protected List<List<String>> includes;
        protected List<List<String>> excludes;
        protected boolean prompt;

        @SuppressWarnings("unchecked")
        public TargetStep(Map<String, Object> map) {
            this.prompt = (boolean) map.get("prompt");
            this.includes = (List<List<String>>) map.get("includes");
            checkIncludes();
            this.excludes = (List<List<String>>) map.get("excludes");
        }

        public TargetStep(Step other) {
            super(other);
            TargetStep otherTargetStep = (TargetStep) other;
            this.prompt = otherTargetStep.prompt;
            this.includes = otherTargetStep.includes.stream()
                    .map(include -> include.stream().collect(Collectors.toList()))
                    .collect(Collectors.toList());
            this.excludes = otherTargetStep.excludes.stream()
                    .map(include -> include.stream().collect(Collectors.toList()))
                    .collect(Collectors.toList());
        }

        private void checkIncludes() {
            if(includes == null || includes.size() < 1 || includes.get(0).size() < 1) {
                throw new IllegalArgumentException("includes must not be empty");
            }
            boolean validBase = includes.stream()
                    .allMatch(andTerm ->
                            filterBases.contains(andTerm.get(0))
                    );
            if(!validBase) {
                throw new IllegalArgumentException("the first include must be a legal base. for example: CREATURE or STRUCTURE");
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean run(State state, Card source, Event event, Map<String, Object> value) {
            List<NonSpellCard> potentialTargets = getIncludedTargets(state, source.getController());

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

        private List<NonSpellCard> getIncludedTargets(State state, Player controllingPlayer) {
            List<NonSpellCard> allIncludedTargets = new ArrayList<>();
            List<Player> playingPlayers = state.getPlayingPlayers();
            includes.stream()
                    .forEach( andTerm -> {
                        String base = andTerm.get(0);
                        Stream<NonSpellCard> includedTargetsStream;
                        //get base
                        switch (base) {
                            case "CREATURE":
                                includedTargetsStream = playingPlayers.stream()
                                        .map(player -> player.getField().getCards())
                                        .flatMap(Collection::stream);
                                break;
                            case "STRUCTURE":
                                includedTargetsStream = playingPlayers.stream()
                                        .map(player -> player.getCourtyard().getCards())
                                        .flatMap(Collection::stream);
                                break;
                            case "CASTLE":
                                includedTargetsStream = playingPlayers.stream()
                                        .map(Player::getCastle);
                                break;
                            default:
                                throw new IllegalStateException("Base isn't recognized");
                        }
                        //handle additional conditions
                        if(andTerm.contains("ENEMY")) {
                            includedTargetsStream = includedTargetsStream
                                    .filter(includedTarget -> !includedTarget.getController().equals(controllingPlayer));
                        } else if(andTerm.contains("FRIENDLY")) {
                            includedTargetsStream = includedTargetsStream
                                    .filter(includedTarget -> includedTarget.getController().equals(controllingPlayer));
                        }

                        allIncludedTargets.addAll(includedTargetsStream.collect(Collectors.toList()));
                    });

            return allIncludedTargets;
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

    public TargetResult(Result other) {
        super(other);
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value = super.prepareNewValue();
        value.put("targets", new ArrayList<NonSpellCard>());
        return value;
    }
}
