package nl.tcilegnar.timer.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import nl.tcilegnar.timer.BuildConfig;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public abstract class MyBuildConfigTest {
    protected MyBuildConfig buildConfig;

    @Before
    public void setUp() {
        buildConfig = new MyBuildConfig();
    }

    @Test
    public abstract void isProduction_DependsOnBuildType();

    @Test
    public abstract void isTest_DependsOnBuildType();

    @Test
    public abstract void isDevelop_DependsOnBuildType();
}