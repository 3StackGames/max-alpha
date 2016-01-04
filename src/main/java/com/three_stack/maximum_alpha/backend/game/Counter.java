package com.three_stack.maximum_alpha.backend.game;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class Counter {
    private long id;
    private String name;
    private Card source;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card getSource() {
        return source;
    }

    public void setSource(Card source) {
        this.source = source;
    }
}
