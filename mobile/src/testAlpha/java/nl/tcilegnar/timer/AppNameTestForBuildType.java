package nl.tcilegnar.timer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import nl.tcilegnar.timer.utils.Res;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AppNameTestForBuildType extends AppNameTestForFlavor {
    @Test
    public void getString_AppName_ShouldContainBuildType() {
        // Arrange

        // Act
        String actualAppName = Res.getString(R.string.app_name);

        // Assert
        assertTrue(actualAppName.contains("alpha"));
    }
}
