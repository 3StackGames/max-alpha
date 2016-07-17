package com.three_stack.maximum_alpha.backend.game.effects.events;

import com.three_stack.maximum_alpha.backend.game.Time;
import com.three_stack.maximum_alpha.backend.game.player.Player;

public class PlayerEvent extends Event {
    protected final Player player;

    public PlayerEvent(Time time, String type, Player player) {
        super(time, type);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
