package com.three_stack.maximum_alpha.backend.game.effects.prompts;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.player.Player;
import io.gsonfire.annotations.ExposeMethodResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Prompt {
    protected String description;
    protected transient Card source;
    protected transient Player player;
    protected boolean isMandatory;
    protected transient Map<String, Object> value;
    /**
     * event is the event that caused the prompt. For example, an event triggered by ON_PLAY.
     */
    protected Event event;

    public Prompt(String description, Card source, Player player, Event event, boolean isMandatory, Map<String, Object> value) {
        this.description = description;
        this.source = source;
        this.event = event;
        this.player = player;
        this.isMandatory = isMandatory;
        this.value = value;
    }

    public abstract void resolve(State state);

    public Card getSource() {
        return source;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public abstract boolean isValidInput(Object input);

    /**
     * @TODO: Richard, I broke this and I'm not sure what's necessary to fix this - Sincerely, J
     */
    @ExposeMethodResult("canUndo")
    public boolean canUndo() {
//        return !isMandatory() || !promptSteps.isEmpty();
        return false;
    }

    /**
     * Undoes the current step.
     * @Todo: Richard, same deal as above
     * @return True if the prompt should be removed from the queue, false otherwise.
     */
    public boolean undo() {
        /*
        if (promptSteps.size() > 0) {
        	currentPromptStep = promptSteps.pop();
            getCurrentPromptStep().reset();
            return false;
        } else {
            return true;
        }
        */
        return false;
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
