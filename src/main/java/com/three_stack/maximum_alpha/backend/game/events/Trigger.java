package com.three_stack.maximum_alpha.backend.game.events;

public enum Trigger {
    ON_ENTER_HAND,
    ON_DRAW,
    ON_PULL,
    ON_LEAVE_HAND,
    ON_PLAY,
    /**
     * Spells occur ON_CAST
     */
    ON_CAST,
    ON_ASSIGN,
    ON_ATTACK,
    ON_BLOCK,
    ON_DAMAGE,
    ON_TARGET,
    ON_STRUCTURE_COMPLETE,
    ON_ENTER_FIELD,
    ON_DEATH,
    ON_REFRESH,
    ON_EXHAUST,

    ON_BEGIN_PHASE_START,
    ON_ATTACK_PHASE_START,
    ON_BLOCK_PHASE_END,
    ON_END_PHASE_END
}
