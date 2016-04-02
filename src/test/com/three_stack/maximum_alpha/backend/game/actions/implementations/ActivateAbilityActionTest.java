package com.three_stack.maximum_alpha.backend.game.actions.implementations;

import com.three_stack.maximum_alpha.backend.game.State;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;


public class ActivateAbilityActionTest {
    private State state;
    
    @Before
    public void setUp() throws Exception {
        mockState();
    }

    @Test
    public void testIsValid_ShouldReturnFalse() throws Exception {

    }

    private void mockState() {
        state = mock(State.class);
    }
}
