package nl.tcilegnar.timer.utils;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;

public class MyBuildConfigTestForAlpha extends MyBuildConfigTest {
    @Override
    public void isProduction_DependsOnBuildType() {
        assertFalse(buildConfig.isProduction());
    }

    @Override
    public void isTest_DependsOnBuildType() {
        assertTrue(buildConfig.isTest());
    }

    @Override
    public void isDevelop_DependsOnBuildType() {
        assertFalse(buildConfig.isDevelop());
    }
}