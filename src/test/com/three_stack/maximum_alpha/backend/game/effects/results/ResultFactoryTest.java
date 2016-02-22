package com.three_stack.maximum_alpha.backend.game.effects.results;

import org.junit.Assert;
import org.junit.Test;

public class ResultFactoryTest {
    @Test
    public void test_getResultClassName_shouldReturnClassName() throws Exception {
        String result = ResultFactory.getResultClassName("DEAL_DAMAGE");
        Assert.assertEquals(result, "com.three_stack.maximum_alpha.backend.game.effects.results.implementations.DealDamageResult");
    }
}
