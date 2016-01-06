package com.three_stack.maximum_alpha.backend.game.prompts;

import com.three_stack.maximum_alpha.backend.game.cards.Card;

public abstract class Prompt {
    protected Card source;

    protected String prompt;

    public Prompt(Card source, String prompt) {
        this.source = source;
        this.prompt = prompt;
    }

    public Card getSource() {
        return source;
    }

    public void setSource(Card source) {
        this.source = source;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
