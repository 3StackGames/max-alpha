package com.three_stack.maximum_alpha.backend.game;

import com.three_stack.maximum_alpha.backend.game.player.ResourceList;
import com.three_stack.maximum_alpha.backend.game.victories.DefaultVictoryHandler;
import com.three_stack.maximum_alpha.backend.game.victories.VictoryHandler;

/**
 * Data object class holding the settings for a game state.
 */
public class Parameters {
    public final int initialHandSize = 5;
    public final int maxHealth = 40;
    public final ResourceList initialResources = new ResourceList(1);
    public final VictoryHandler victoryHandler = new DefaultVictoryHandler();

    public Parameters() {
    }
}
