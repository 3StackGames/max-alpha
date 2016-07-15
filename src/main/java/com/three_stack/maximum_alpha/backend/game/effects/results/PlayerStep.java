package com.three_stack.maximum_alpha.backend.game.effects.results;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.PlayerPrompt;
import com.three_stack.maximum_alpha.backend.game.effects.prompts.Prompt;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class PlayerStep extends Step {
    //number of targets the prompt should find
    protected List<List<String>> includes;
    protected List<List<String>> excludes;
    protected boolean prompt;
    protected boolean self;

    @SuppressWarnings("unchecked")
    public PlayerStep(Map<String, Object> map) {
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

    public PlayerStep(boolean self, boolean prompt, List<List<String>> includes, List<List<String>> excludes) {
        setup();
        this.self = self;
        this.prompt = prompt;
        this.includes = includes;
        this.excludes = excludes;
    }

    public PlayerStep(Step other) {
        super(other);
        PlayerStep otherStep = (PlayerStep) other;
        this.prompt = otherStep.prompt;
        this.self = otherStep.self;
        this.includes = otherStep.includes.stream()
                .map(include -> include.stream().collect(Collectors.toList()))
                .collect(Collectors.toList());
        this.excludes = otherStep.excludes.stream()
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
            List<Player> players = (List<Player>) value.get("players");
            players.add(source.getController());
            return false;
        } else {
            List<Player> potentialPlayers = getIncludedPlayers(state, source.getController());

            if(prompt) {
            	if(potentialPlayers.size() == 0)
            		return false;
                //@Todo: make this based on the database description
                String description = "Select a player";
                Prompt prompt = new PlayerPrompt(description, source, source.getController(), event, mandatory, value, potentialPlayers);
                state.addPrompt(prompt);
                return true;
            } else {
                List<Player> players = (List<Player>) value.get("players");
                players.addAll(potentialPlayers);
                return false;
            }
        }
    }

    private List<Player> getIncludedPlayers(State state, Player controllingPlayer) {
        List<Player> allIncludedPlayers = new ArrayList<>();
        includes.stream()
                .forEach( andTerm -> {
                    switch (andTerm.get(0)) {
                        case "SELF":
                            allIncludedPlayers.add(controllingPlayer);
                            break;
                        case "ENEMY":
                        	allIncludedPlayers.addAll(state.getPlayersExcept(controllingPlayer));
                            break;
                        case "ALL":
                        	allIncludedPlayers.addAll(state.getPlayingPlayers());
                            break;
                        case "ALLY":
                        	//TODO add ally functionality? (not for MVP)
                        	break;
                        default:
                            throw new IllegalStateException("Include term isn't recognized");
                    }
                });

        return allIncludedPlayers;
    }

}
