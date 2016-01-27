package com.three_stack.maximum_alpha.backend.game.effects.prompts;

import com.three_stack.maximum_alpha.backend.game.ResourceList;
import com.three_stack.maximum_alpha.backend.game.cards.Card;
import com.three_stack.maximum_alpha.backend.game.cards.Spell;
import com.three_stack.maximum_alpha.backend.game.effects.Effect;
import com.three_stack.maximum_alpha.backend.game.effects.Results;

/**
 * Factory that creates spells purely for prompts to be used as choices.
 * @Todo: find a way to make this pull from the database instead. consider nested cards within the database
 * ie: wrath has the other two cards embeded in itself
 */
public class PromptSpellFactory {
    public static Spell createDamageAllEnemyCastles(Card source, int damage) {
        Spell damageEnemyCastles = new Spell("Damage All Enemy Castles", new ResourceList(), "Deal " + damage + " damage to all enemy castles", "");

        Effect effect = new Effect(source);
        effect.addResult(Results.DEAL_DAMAGE_ENEMY_CASTLES, damage);
        damageEnemyCastles.addEffect(effect);

        return damageEnemyCastles;
    }

    public static Spell createDamageAllCreatures(Card source, int damage) {
        Spell damageAllCreatures = new Spell("Damage All Creatures", new ResourceList(), "Deal " + damage + " damage to all creatures", "");

        Effect effect = new Effect(source);
        effect.addResult(Results.DEAL_DAMAGE_ALL_CREATURES, damage);
        damageAllCreatures.addEffect(effect);

        return damageAllCreatures;
    }
}
