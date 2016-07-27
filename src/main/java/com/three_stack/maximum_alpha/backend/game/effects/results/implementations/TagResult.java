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
import com.three_stack.maximum_alpha.backend.game.utilities.ValueExpression;
import com.three_stack.maximum_alpha.database_client.pojos.DBResult;

public class TagResult extends TargetResult {
	String tagName;
	boolean remove;
	ValueExpression valueE;
	
	//If remove == true, removes the tag, else adds the tag
	public TagResult(List<TargetStep> targetSteps, String tagName, ValueExpression valueE, boolean remove) {
        super(targetSteps);
        this.tagName = tagName;
        this.remove = remove;
        this.valueE = valueE;
    }

    public TagResult(DBResult dbResult) {
        super(dbResult);
        if(dbResult.getValue().get("value") != null)
        	valueE = new ValueExpression(dbResult.getValue().get("value"));
        this.tagName = (String) dbResult.getValue().get("tag");
        this.remove = (boolean) dbResult.getValue().get("remove");
    }

    public TagResult(Result other) {
        super(other);
        TagResult otherResult = (TagResult) other;
        this.tagName = otherResult.tagName;
        this.remove = otherResult.remove;
        this.valueE = otherResult.valueE;
    }

    @Override
    public Map<String, Object> prepareNewValue() {
        Map<String, Object> value = super.prepareNewValue();
        value.put("tagName", tagName);
        value.put("remove", remove);
        value.put("valueE", valueE);
        return value;
    }

	@Override
	public void resolve(State state, Card source, Event event,
			Map<String, Object> value) {
        List<NonSpellCard> targets = (List<NonSpellCard>) value.get("targets");
        int valueAmount = 0;
        if (valueE != null)
          valueAmount = valueE.eval(state, source);
        Tag tag = new Tag(TagType.valueOf(tagName), valueAmount);
        
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
