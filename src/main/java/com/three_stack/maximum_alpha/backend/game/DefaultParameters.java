package com.three_stack.maximum_alpha.backend.game;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.victories.DefaultVictoryHandler;
import com.three_stack.maximum_alpha.backend.game.victories.VictoryHandler;
import com.three_stack.maximum_alpha.backend.server.Connection;

public class DefaultParameters {
    final int INITIAL_DRAW_SIZE = 5;
    final int TOTAL_HEALTH = 1;
    public final static int INITIAL_COLORLESS_MANA = 1;
    final VictoryHandler victoryHandler = new DefaultVictoryHandler();

    public List<Connection> players;

    public DefaultParameters(List<Connection> players) {
        this.players = players;
    }
}
