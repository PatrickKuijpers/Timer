package nl.tcilegnar.timer;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class BuildConfigTestForRelease extends BuildConfigTestForProductFlavor {
    private static final String APPLICATION_ID = APPLICATION_ID_WITH_FLAVOR + "";

    @Override
    public void BuildConfig_ApplicationId_SuffixDependsOnBuildType() {
        // Arrange

        // Act
        String actualApplicationId = BuildConfig.APPLICATION_ID;

        // Assert
        assertEquals(APPLICATION_ID, actualApplicationId);
    }
}
