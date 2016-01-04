package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.events.Event;

public interface Damageable {
    Event takeDamage(int damage, Card source);
}
