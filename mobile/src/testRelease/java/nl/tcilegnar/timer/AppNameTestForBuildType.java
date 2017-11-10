package nl.tcilegnar.timer;

import org.junit.Test;

public abstract class AppNameTestForBuildType extends AppNameTestForFlavor {
    @Test
    public void getString_AppName_ShouldContainBuildType() {
        getString_AppName_ShouldBeCorrectForRelease();
    }

    @Test
    public abstract void getString_AppName_ShouldBeCorrectForRelease();
}
