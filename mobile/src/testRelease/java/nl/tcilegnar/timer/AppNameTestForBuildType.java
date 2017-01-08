package nl.tcilegnar.timer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public abstract class AppNameTestForBuildType extends AppNameTestForFlavor {
    @Test
    public void getResourceString_AppName_ShouldContainBuildType() {
        getResourceString_AppName_ShouldBeCorrectForRelease();
    }

    @Test
    public abstract void getResourceString_AppName_ShouldBeCorrectForRelease();
}
