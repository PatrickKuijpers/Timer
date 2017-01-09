package nl.tcilegnar.timer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import nl.tcilegnar.timer.utils.Res;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public abstract class AppNameTest {
    @Test
    public void getString_AppName_ShouldContainBaseAppName() {
        // Arrange

        // Act
        String actualAppName = Res.getString(R.string.app_name);

        // Assert
        assertTrue(actualAppName.contains("Timer"));
    }

    @Test
    public abstract void getString_AppName_ShouldContainFlavorName();

    @Test
    public abstract void getString_AppName_ShouldContainBuildType();
}
