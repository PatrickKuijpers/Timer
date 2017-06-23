package nl.tcilegnar.dndcharactersheet.Utils;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import nl.tcilegnar.timer.BuildConfig;
import nl.tcilegnar.timer.utils.MyBuildConfigTest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
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