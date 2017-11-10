package nl.tcilegnar.timer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public abstract class BuildConfigTest {
    protected static final String APPLICATION_ID_BASE = "nl.tcilegnar.timer";

    @Test
    public void BuildConfig_ApplicationId_BaseShouldContainNlTcilegnarTimer() {
        // Arrange

        // Act
        String actualApplicationId = BuildConfig.APPLICATION_ID;

        // Assert
        assertTrue(actualApplicationId.contains(APPLICATION_ID_BASE));
    }

    @Test
    public abstract void BuildConfig_ApplicationId_SuffixDependsOnProductFlavor();

    @Test
    public abstract void BuildConfig_ApplicationId_SuffixDependsOnBuildType();
}
