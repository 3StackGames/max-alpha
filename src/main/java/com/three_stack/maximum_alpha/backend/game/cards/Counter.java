package com.three_stack.maximum_alpha.backend.game.cards;

public class Counter {
    private final long id;
    private final String name;
    private final Card source;

    public Counter(long id, String name, Card source) {
      this.id = id;
      this.name = name;
      this.source = source;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Card getSource() {
        return source;
    }
}
