package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.database_client.pojos.DBTag;

/**
 * Immutable
 */
final public class Tag {
    public enum TagType {
        QUICK,
        PIERCE,
        LEGENDARY,
        AIRBORNE,
        ANTI_AIR,
        LETHAL,
        DEGENERATE,
        MUTATE,
        GUARD,
        GROWTH
    }
    final private TagType type;
    final private Integer value;

    public Tag(TagType name, Integer value) {
        this.type = name;
        this.value = value;
    }

    public Tag(DBTag dbTag) {
        this.type = TagType.valueOf(dbTag.getType());
        this.value = dbTag.getValue();
    }

    public TagType getType() {
        return type;
    }

    public Integer getValue() {
        return value;
    }
}
