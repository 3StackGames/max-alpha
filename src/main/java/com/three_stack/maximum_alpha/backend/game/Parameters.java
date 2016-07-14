package com.three_stack.maximum_alpha.backend.game;

import java.util.List;

import com.three_stack.maximum_alpha.backend.game.victories.DefaultVictoryHandler;
import com.three_stack.maximum_alpha.backend.game.victories.VictoryHandler;
import com.three_stack.maximum_alpha.backend.server.Connection;

/**
 * Data object class to initialize a game with.
 */
public class Parameters {
    public final int INITIAL_DRAW_SIZE = 5;
    public final int TOTAL_HEALTH = 40;
    public final static int INITIAL_COLORLESS_RESOURCES = 1; //Temporary, for testing purposes
    public final VictoryHandler victoryHandler = new DefaultVictoryHandler();

    public List<Connection> players;

    public Parameters(List<Connection> players) {
        this.players = players;
    }
}
