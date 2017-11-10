package nl.tcilegnar.timer;

import org.junit.Test;

import nl.tcilegnar.timer.utils.Res;

import static junit.framework.Assert.assertTrue;

public abstract class AppNameTestForFlavor extends AppNameTest {
    @Test
    public void getString_AppName_ShouldContainFlavorName() {
        // Arrange

        // Act
        String actualAppName = Res.getString(R.string.app_name);

        // Assert
        assertTrue(actualAppName.contains("work"));
    }
}
