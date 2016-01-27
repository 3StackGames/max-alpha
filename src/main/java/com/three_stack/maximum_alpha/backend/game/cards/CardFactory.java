package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
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

        Card card;
        switch (dbCard.getType()) {
            case("creature"):
                int creatureAttack = dbCard.getAttack();
                int creatureHealth = dbCard.getHealth();
                card = new Creature(name, cost, text, flavorText, creatureAttack, creatureHealth);
                break;
            case("structure"):
                int structureHealth = dbCard.getHealth();
                card = new Structure(name, cost, text, flavorText, structureHealth);
                break;
            case("spell"):
                Spell spell = new Spell(name, cost, text, flavorText);
                final Card finalCard = spell;
                List<Effect> effects = dbCard.getEffects().stream()
                        .map(dbEffect -> new Effect(finalCard, dbEffect))
                        .collect(Collectors.toList());
                spell.setEffects(effects);
                card = spell;
                break;
            default:
                throw new IllegalArgumentException("Card type is invalid");
        }

        final Card finalCard = card;
        Map<Trigger, List<Effect>> triggerEffects = dbCard.getTriggerEffects().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> Trigger.valueOf(e.getKey()),
                        e -> e.getValue().stream()
                                .map(dbEffect -> new Effect(finalCard, dbEffect))
                                .collect(Collectors.toList())
                ));
        card.setTriggerEffects(triggerEffects);
        return card;
    }
}
