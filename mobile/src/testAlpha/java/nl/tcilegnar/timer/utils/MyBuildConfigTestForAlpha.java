package nl.tcilegnar.timer.utils;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import nl.tcilegnar.timer.BuildConfig;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
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