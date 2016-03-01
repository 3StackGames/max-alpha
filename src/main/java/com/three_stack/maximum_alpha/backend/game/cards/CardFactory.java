package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.effects.Trigger;
import com.three_stack.maximum_alpha.database_client.pojos.DBAbility;
import com.three_stack.maximum_alpha.database_client.pojos.DBCard;
import com.three_stack.maximum_alpha.database_client.pojos.DBEffect;
import com.three_stack.maximum_alpha.database_client.pojos.DBTag;

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
                Creature creature = new Creature(name, cost, text, flavorText, creatureAttack, creatureHealth);
                setNonSpellCardAttributes(creature, dbCard);
                card = creature;
                break;
            case("structure"):
                int structureHealth = dbCard.getHealth();
                Structure structure = new Structure(name, cost, text, flavorText, structureHealth);
                setNonSpellCardAttributes(structure, dbCard);
                card = structure;
                break;
            case("spell"):
                Spell spell = new Spell(name, cost, text, flavorText);
                setEffects(spell, dbCard.getEffects());
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

    private static void setNonSpellCardAttributes(NonSpellCard card, DBCard dbCard) {
        setAbilities(card, dbCard.getAbilities());
        setClasses(card, dbCard.getClasses());
        setTags(card, dbCard.getTags());
    }

    private static void setAbilities(NonSpellCard card, List<DBAbility> dbAbilities) {
        if(dbAbilities == null) {
            return;
        }
        List<Ability> abilities =  dbAbilities.stream()
                .map(dbAbility -> new Ability(card, dbAbility))
                .collect(Collectors.toList());

        card.setAbilities(abilities);
    }

    private static void setClasses(NonSpellCard card, List<String> classStrings) {
        List<CardClass> cardClasses = classStrings.stream()
                .map(CardClass::valueOf)
                .collect(Collectors.toList());
        card.setClasses(cardClasses);
    }

    private static void setTags(NonSpellCard card, List<DBTag> dbTags) {
        List<Tag> tags = dbTags.stream()
                .map(Tag::new)
                .collect(Collectors.toList());
        card.setTags(tags);
    }

    private static void setEffects(Spell spell, List<DBEffect> dbEffects) {
        List<Effect> effects = dbEffects.stream()
                .map(dbEffect -> new Effect(spell, dbEffect))
                .collect(Collectors.toList());
        spell.setEffects(effects);
    }
}
