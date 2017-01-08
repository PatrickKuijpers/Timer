package nl.tcilegnar.timer;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AppTest {
    @Test
    public void getContext_ShouldBeApplicationContext() {
        // Arrange

        // Act
        Context actualContext = App.getContext();

        // Assert
        assertEquals(getApplicationContext(), actualContext);
    }

    @Test
    public void getAppResources_ShouldBeResourcesFromApplicationContext() {
        // Arrange

        // Act
        Resources actualResources = App.getAppResources();

        // Assert
        assertEquals(getApplicationResources(), actualResources);
    }

    @Test
    public void getAppResources_ShouldContainSameStringAsGetStringFromApplicationResources() {
        // Arrange

        // Act
        String actualAppName = App.getResourceString(R.string.app_name);

        // Assert
        String expectedAppName = getApplicationResources().getString(R.string.app_name);
        assertEquals(expectedAppName, actualAppName);
    }

    @Test
    public void getResourceString_ShouldContainSameStringAsGetStringFromApplicationResources() {
        // Arrange

        // Act
        String actualString = App.getResourceString(R.string.app_name);

        // Assert
        String expectedString = getApplicationResources().getString(R.string.app_name);
        assertEquals(expectedString, actualString);
    }

//    @Test
//    public void getResourceInteger_ShouldContainSameIntegerAsGetIntegerFromApplicationResources() {
//        // Arrange
//
//        // Act
//        Integer actualInteger = App.getResourceInteger(R.integer.max_length_bronze_value);
//
//        // Assert
//        Integer expectedInteger = getApplicationResources().getInteger(R.integer.max_length_bronze_value);
//        assertEquals(expectedInteger, actualInteger);
//    }

    @Test
    public void getResourceInteger_ShouldContainSameDimenAsGetDimensionFromApplicationResourcesDividedByDensity() {
        // Arrange

        // Act
        int actualDimension = App.getResourceDimension(R.dimen.horizontal_margin);

        // Assert
        float density = getApplicationResources().getDisplayMetrics().density;
        float dimension = getApplicationResources().getDimension(R.dimen.horizontal_margin);
        int expectedDimension = (int) (dimension / density);
        assertEquals(expectedDimension, actualDimension);
    }

    //    @Test
    //    public void getResourceBoolean_ShouldContainSameBooleanAsGetBooleanFromApplicationResources() {
    //        // Arrange
    //
    //        // Act
    //        boolean bool = App.getResourceBoolean(R.bool.some_boolean);
    //
    //        // Assert
    //        boolean expectedBool = getApplicationResources().getBoolean(R.bool.some_boolean);
    //        assertEquals(expectedBool, bool);
    //    }

    @Test
    public void getResourceColor_ShouldContainSameColorAsGetColorFromApplicationContext() {
        // Arrange

        // Act
        int actualColorInt = App.getResourceColor(R.color.colorAccent);

        // Assert
        int expectedColorInt = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
        assertEquals(expectedColorInt, actualColorInt);
    }

    private Resources getApplicationResources() {
        return getApplicationContext().getResources();
    }

    private Context getApplicationContext() {
        return RuntimeEnvironment.application.getApplicationContext();
    }
}
