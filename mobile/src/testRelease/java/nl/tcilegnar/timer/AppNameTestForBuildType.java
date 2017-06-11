package nl.tcilegnar.timer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public abstract class AppNameTestForBuildType extends AppNameTestForFlavor {
    @Test
    public void getString_AppName_ShouldContainBuildType() {
        getString_AppName_ShouldBeCorrectForRelease();
    }

    @Test
    public abstract void getString_AppName_ShouldBeCorrectForRelease();
}
