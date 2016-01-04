package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.backend.game.event.Event;

public interface Damageable {
    Event takeDamage(int damage, Card source);
}
