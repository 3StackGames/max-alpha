package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.cards.Tag.TagType;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.TargetPrompt;
import com.three_stack.maximum_alpha.backend.game.player.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TargetStep extends Step {
    //@Todo: don't require a base
    protected static final List<String> filterBases = Arrays.asList("CREATURE", "STRUCTURE", "CASTLE");

    //number of targets the prompt should find
    protected List<List<String>> includes;
    protected List<List<String>> excludes;
    protected boolean prompt;
    protected boolean self;

    @SuppressWarnings("unchecked")
    public TargetStep(Map<String, Object> map) {
        setup();
        if(map.containsKey("self")) {
            this.self = (boolean) map.get("self");
        }
        if(!self) {
            this.prompt = (boolean) map.get("prompt");
            this.includes = (List<List<String>>) map.get("includes");
            checkIncludes();
            this.excludes = (List<List<String>>) map.get("excludes");
            checkExcludes();
        }
    }

    public TargetStep(boolean self, boolean prompt, List<List<String>> includes, List<List<String>> excludes) {
        setup();
        this.self = self;
        this.prompt = prompt;
        this.includes = includes;
        this.excludes = excludes;
    }

    public TargetStep(Step other) {
        super(other);
        TargetStep otherTargetStep = (TargetStep) other;
        this.prompt = otherTargetStep.prompt;
        this.self = otherTargetStep.self;
        this.includes = otherTargetStep.includes.stream()
                .map(include -> include.stream().collect(Collectors.toList()))
                .collect(Collectors.toList());
        this.excludes = otherTargetStep.excludes.stream()
                .map(exclude -> exclude.stream().collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private void setup() {
        this.self = false;
        this.prompt = false;
        this.includes = new ArrayList<>();
        this.excludes = new ArrayList<>();
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
    
    //TODO: implement for real
    private void checkExcludes() {
        if(excludes == null || excludes.size() < 1 || excludes.get(0).size() < 1) {
            excludes = new ArrayList<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean run(State state, Card source, Event event, Map<String, Object> value) {
        if(self) {
            List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
            targets.add((NonSpellCard) source);
            return false;
        } else {
            List<NonSpellCard> potentialTargets = getIncludedTargets(state, source.getController());

            if(prompt) {
            	if(potentialTargets.size() == 0)
            		return false;
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
                    //TODO: friendly should mean "allied to controlling player" (not for MVP)
                    if(andTerm.contains("ENEMY")) {
                        includedTargetsStream = includedTargetsStream
                                .filter(includedTarget -> !includedTarget.getController().equals(controllingPlayer));
                    } else if(andTerm.contains("FRIENDLY")) {
                        includedTargetsStream = includedTargetsStream
                                .filter(includedTarget -> includedTarget.getController().equals(controllingPlayer));
                    } else if(andTerm.contains("OWN")) {
                        includedTargetsStream = includedTargetsStream
                                .filter(includedTarget -> includedTarget.getController().equals(controllingPlayer));
                    }

                    allIncludedTargets.addAll(includedTargetsStream.filter(target -> !target.hasTag(TagType.UNTARGETABLE)).collect(Collectors.toList()));
                });

        return allIncludedTargets;
    }
}

