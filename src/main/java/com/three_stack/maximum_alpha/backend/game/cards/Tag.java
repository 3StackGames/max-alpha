package com.three_stack.maximum_alpha.backend.game.cards;

import com.three_stack.maximum_alpha.database_client.pojos.DBTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        GROWTH,
        UNTARGETABLE
    }
    final private TagType type;
    final private Integer value;
    final private List<UUID> effectIds;

    /**
     * Usually you'll want to use this one. Let the setter handle effectIds
     * @param type
     * @param value
     */
    public Tag(TagType type, Integer value) {
        this.type = type;
        this.value = value;
        this.effectIds = new ArrayList<>();
    }

    protected Tag(TagType type, Integer value, List<UUID> effectIds) {
        this.type = type;
        this.value = value;
        this.effectIds = effectIds;
    }

    public Tag(DBTag dbTag) {
        this.type = TagType.valueOf(dbTag.getType());
        this.value = dbTag.getValue();
        this.effectIds = new ArrayList<>();;
    }

    public TagType getType() {
        return type;
    }

    public Integer getValue() {
        return value;
    }

    public List<UUID> getEffectIds() {
        return effectIds;
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
