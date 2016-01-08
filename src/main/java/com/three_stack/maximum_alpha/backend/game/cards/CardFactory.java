package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import com.three_stack.maximum_alpha.backend.game.events.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardFactory {
    public static Card create(Document cardDocument) {
        String name = cardDocument.getString("name");
        String text = cardDocument.getString("text");
        String flavorText = cardDocument.getString("flavorText");
        Map<String, Integer> costMap = (Map) cardDocument.get("cost");
        ResourceList cost = new ResourceList(costMap);
        Map<Trigger, List<Effect>> effects = parseEffects(cardDocument.get("effects"));

        String type = cardDocument.getString("type");
        switch(type) {
            case("creature"):
                int creatureAttack = cardDocument.getInteger("attack");
                int creatureHealth = cardDocument.getInteger("health");
                return new Creature(name, cost, text, flavorText, creatureAttack, creatureHealth, effects);

            case("structure"):
                int structureHealth = cardDocument.getInteger("health");
                return new Structure(name, cost, text, flavorText, structureHealth, effects);

            default:
                throw new IllegalArgumentException("Card type is invalid");
        }
    }

    private static Map<Trigger, List<Effect>> parseEffects(Object effectsObject) {
        if(effectsObject == null)
            return null;
        Map<String, List<Document>> effectsDocumentMap = (Map<String, List<Document>>) effectsObject;
        Map<Trigger, List<Effect>> parsedEffects = new HashMap<>();

        for (Map.Entry<String, List<Document>> triggerEffect : effectsDocumentMap.entrySet()) {
            String triggerName = triggerEffect.getKey();
            List<Document> effectList = triggerEffect.getValue();

            Trigger trigger = Trigger.valueOf(triggerName);
            List<Effect> triggeredEffects = new ArrayList<>();

            for(Document effect : effectList) {
                String checkName = effect.getString("check");
                String resultName = effect.getString("result");
                Object value = effect.get("value");
                Check check = null;
                Result result = null;

                try {
                    check = (Check) Checks.class.getField(checkName).get(Checks.class.newInstance());
                    result = (Result) Results.class.getField(resultName).get(Results.class.newInstance());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                triggeredEffects.add(new Effect(null, check, result, value));
            }

            parsedEffects.put(trigger, triggeredEffects);
        }

        return parsedEffects;
    }
}
