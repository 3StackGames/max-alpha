package com.three_stack.maximum_alpha.backend.game;

import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;

public class ResourceListTest {
    protected ResourceList nothing;
    protected ResourceList blueColorless;
    protected ResourceList colorlessColorless;
    @Before
    public void setUp() throws Exception {
        nothing = new ResourceList(0, 0, 0, 0, 0, 0, 0);
        blueColorless = new ResourceList(0, 0, 0, 0, 0, 1, 1);
        colorlessColorless = new ResourceList(0, 0, 0, 0, 0, 0, 2);
    }

    @org.junit.Test
    public void testResourceList_ShouldHaveResources() throws Exception {
        Validate.isTrue(blueColorless.hasResources(colorlessColorless));
        Validate.isTrue(blueColorless.hasResources(nothing));

        Validate.isTrue(colorlessColorless.hasResources(nothing));
    }

    @Test
    public void testResourceList_ShouldNotHaveResources() throws Exception {
        Validate.isTrue(!nothing.hasResources(blueColorless));
        Validate.isTrue(!nothing.hasResources(colorlessColorless));

        Validate.isTrue(!colorlessColorless.hasResources(blueColorless));


    }
}
