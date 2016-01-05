package com.three_stack.maximum_alpha.backend.game;

import java.util.List;

import com.three_stack.maximum_alpha.backend.server.Connection;

public class Parameters {
    final int INITIAL_DRAW_SIZE = 7;
    final int TOTAL_HEALTH = 40;
    public final static int INITIAL_COLORLESS_MANA = 1;

    public List<Connection> players;

    public Parameters(List<Connection> players) {
        this.players = players;
    }
}
