package nl.tcilegnar.timer;

import static junit.framework.Assert.assertTrue;

public abstract class BuildConfigTestForProductFlavor extends BuildConfigTest {
    protected static final String APPLICATION_ID_WITH_FLAVOR = APPLICATION_ID_BASE + ".sleep";

    @Override
    public void BuildConfig_ApplicationId_SuffixDependsOnProductFlavor() {
        // Arrange

        // Act
        String actualApplicationId = BuildConfig.APPLICATION_ID;

        // Assert
        assertTrue(actualApplicationId.contains(APPLICATION_ID_WITH_FLAVOR));
    }
}
