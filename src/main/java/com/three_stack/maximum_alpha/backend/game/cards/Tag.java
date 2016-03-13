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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (type != tag.type) return false;
        return value != null ? value.equals(tag.value) : tag.value == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
