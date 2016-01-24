package com.three_stack.maximum_alpha.backend.game.events.outcomes;

import com.three_stack.maximum_alpha.backend.game.player.Player;

public class PlayerOutcome extends Outcome {
    protected Player player;

    public PlayerOutcome(String type, Player player) {
        super(type);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
