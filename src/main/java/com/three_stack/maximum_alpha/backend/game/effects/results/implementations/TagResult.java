package com.three_stack.maximum_alpha.backend.game.effects.results.implementations;

import java.util.List;
import java.util.Map;

import com.three_stack.maximum_alpha.backend.game.State;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.NonSpellCard;
import com.three_stack.maximum_alpha.backend.game.cards.Tag;
import com.three_stack.maximum_alpha.backend.game.cards.Tag.TagType;
import com.three_stack.maximum_alpha.backend.game.effects.events.Event;
import com.three_stack.maximum_alpha.backend.game.effects.results.Result;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetResult;
import com.three_stack.maximum_alpha.backend.game.effects.results.TargetStep;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

public class TagResult extends TargetResult {
	Tag tag;
	boolean remove;
	
	//If remove == true, removes the tag, else adds the tag
	public TagResult(List<TargetStep> targetSteps, String tagName, int value, boolean remove) {
        super(targetSteps);
        this.tag = new Tag(TagType.valueOf(tagName), value);
        this.remove = remove;
    }

    public TagResult(DBResult dbResult) {
        super(dbResult);  
        String tagName = (String) dbResult.getValue().get("tag");
        int value = 0;
        if(dbResult.getValue().get("value") != null)
        	value = (int) dbResult.getValue().get("value");
        this.tag = new Tag(TagType.valueOf(tagName), value);
        this.remove = (boolean) dbResult.getValue().get("remove");
    }

    public TagResult(Result other) {
        super(other);
        TagResult otherResult = (TagResult) other;
        this.tag = otherResult.tag;
        this.remove = otherResult.remove;
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value = super.prepareNewValue();
        value.put("tag", tag);
        value.put("remove", remove);
        return value;
    }

	@Override
	public void resolve(State state, Card source, Event event,
			Map<String, Object> value) {
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        if(remove) {
	        for(NonSpellCard target : targets) {
	        	target.removeTag(tag);
	        }
        }
        else {
        	for(NonSpellCard target : targets) {
        		target.addTag(tag);
	        }
        }
	}
}
