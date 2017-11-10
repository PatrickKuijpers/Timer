package nl.tcilegnar.timer;

import org.junit.Test;

import nl.tcilegnar.timer.utils.Res;

import static junit.framework.Assert.assertTrue;

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
