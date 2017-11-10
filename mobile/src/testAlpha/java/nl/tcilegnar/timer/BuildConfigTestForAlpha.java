package nl.tcilegnar.timer;

import static junit.framework.Assert.assertEquals;

public class BuildConfigTestForAlpha extends BuildConfigTestForProductFlavor {
    private static final String APPLICATION_ID = APPLICATION_ID_WITH_FLAVOR + ".alpha";

    @Override
    public void BuildConfig_ApplicationId_SuffixDependsOnBuildType() {
        // Arrange

        // Act
        String actualApplicationId = BuildConfig.APPLICATION_ID;

        // Assert
        assertEquals(APPLICATION_ID, actualApplicationId);
    }
}
