package com.three_stack.maximum_alpha.backend.game.actions;

import com.three_stack.maximum_alpha.backend.game.actions.implementations.PullCardAction;
import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Test;

public class ActionServiceTest {
    static final String VALID_ACTION = "Pull Card";
    static final Class VALID_CLASS = PullCardAction.class;

    static final String INVALID_ACTION = "Pll Card";



    @Test
    public void testGetAction_shouldReturnClass() throws Exception {
        Class actionClass = ActionService.getAction(VALID_ACTION);
        Validate.notNull(actionClass);
        Assert.assertEquals(VALID_CLASS, actionClass);
    }

    @Test(expected=ClassNotFoundException.class)
    public void testGetAction_shouldThrowError() throws Exception {
        Class actionClass = ActionService.getAction(INVALID_ACTION);
    }
}
