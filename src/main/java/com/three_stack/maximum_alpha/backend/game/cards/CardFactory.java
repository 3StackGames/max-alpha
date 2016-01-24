package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.effects.*;
import com.three_stack.maximum_alpha.database_client.pojos.DBCard;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CardFactory {
    //@Todo: spells
    public static Card create(DBCard dbCard) {
        String name = dbCard.getName();
        String text = dbCard.getText();
        String flavorText = dbCard.getFlavorText();
        ResourceList cost = new ResourceList(dbCard.getCost());
        Map<Trigger, List<Effect>> effects = dbCard.getEffects().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> Trigger.valueOf(e.getKey()),
                        e -> e.getValue().stream()
                                .map(Effect::new)
                                .collect(Collectors.toList())
                ));

        switch (dbCard.getType()) {
            case("creature"):
                int creatureAttack = dbCard.getAttack();
                int creatureHealth = dbCard.getHealth();
                return new Creature(name, cost, text, flavorText, creatureAttack, creatureHealth, effects);
            case("structure"):
                int structureHealth = dbCard.getHealth();
                return new Structure(name, cost, text, flavorText, structureHealth, effects);
            default:
                throw new IllegalArgumentException("Card type is invalid");
        }
    }
}
