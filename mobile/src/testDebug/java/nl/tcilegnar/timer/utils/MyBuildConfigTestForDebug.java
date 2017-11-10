package nl.tcilegnar.dndcharactersheet.Utils;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import nl.tcilegnar.timer.BuildConfig;
import nl.tcilegnar.timer.utils.MyBuildConfigTest;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MyBuildConfigTestForDebug extends MyBuildConfigTest {
    @Override
    public void isProduction_DependsOnBuildType() {
        assertFalse(buildConfig.isProduction());
    }

    @Override
    public void isTest_DependsOnBuildType() {
        assertFalse(buildConfig.isTest());
    }

    @Override
    public void isDevelop_DependsOnBuildType() {
        assertTrue(buildConfig.isDevelop());
    }
}