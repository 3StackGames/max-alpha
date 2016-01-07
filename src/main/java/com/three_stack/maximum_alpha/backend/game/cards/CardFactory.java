package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Creature;
import org.bson.Document;

import java.util.Map;

public class CardFactory {
    public static Card create(Document cardDocument) {
        String name = cardDocument.getString("name");
        String text = cardDocument.getString("text");
        String flavorText = cardDocument.getString("flavorText");
        Map<String, Integer> costMap = (Map) cardDocument.get("cost");
        ResourceList cost = new ResourceList(costMap);

        String type = cardDocument.getString("type");
        switch(type) {
            case("creature"):
                int creatureAttack = cardDocument.getInteger("attack");
                int creatureHealth = cardDocument.getInteger("health");
                return new Creature(name, cost, text, flavorText, creatureAttack, creatureHealth);

            case("structure"):
                int structureHealth = cardDocument.getInteger("health");
                return new Structure(name, cost, text, flavorText, structureHealth);

            default:
                throw new IllegalArgumentException("Card type is invalid");
        }

    }
}
