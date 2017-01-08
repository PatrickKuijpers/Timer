package nl.tcilegnar.timer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AppNameTestFinal extends AppNameTestForBuildType {
    @Test
    public void getResourceString_AppName_ShouldBeCorrectForRelease() {
        // Arrange

        // Act
        String actualAppName = App.getResourceString(R.string.app_name);

        // Assert
        assertTrue(actualAppName.contains("Timer sleep"));
    }
}
