package nl.tcilegnar.dndcharactersheet.Utils;

import nl.tcilegnar.timer.utils.MyBuildConfigTest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class MyBuildConfigTestForRelease extends MyBuildConfigTest {
    @Override
    public void isProduction_DependsOnBuildType() {
        assertTrue(buildConfig.isProduction());
    }

    @Override
    public void isTest_DependsOnBuildType() {
        assertFalse(buildConfig.isTest());
    }

    @Override
    public void isDevelop_DependsOnBuildType() {
        assertFalse(buildConfig.isDevelop());
    }
}