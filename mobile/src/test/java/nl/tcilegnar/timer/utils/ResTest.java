package nl.tcilegnar.timer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import nl.tcilegnar.timer.BuildConfig;
import nl.tcilegnar.timer.R;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ResTest {

    @Test
    public void getAppResources_ShouldBeResourcesFromApplicationContext() {
        // Arrange

        // Act
        Resources actualResources = Res.getApplicationResources();

        // Assert
        assertEquals(getApplicationResources(), actualResources);
    }

    @Test
    public void getResourceString_ShouldContainSameStringAsGetStringFromApplicationResources() {
        // Arrange

        // Act
        String actualString = Res.getString(R.string.app_name);

        // Assert
        String expectedString = getApplicationResources().getString(R.string.app_name);
        assertEquals(expectedString, actualString);
    }

//    @Test
//    public void getResourceInteger_ShouldContainSameIntegerAsGetIntegerFromApplicationResources() {
//        // Arrange
//
//        // Act
//        Integer actualInteger = Res.getResourceInteger(R.integer.max_length_bronze_value);
//
//        // Assert
//        Integer expectedInteger = getApplicationResources().getInteger(R.integer.max_length_bronze_value);
//        assertEquals(expectedInteger, actualInteger);
//    }

    @Test
    public void getResourceInteger_ShouldContainSameDimenAsGetDimensionFromApplicationResourcesDividedByDensity() {
        // Arrange

        // Act
        int actualDimension = Res.getDimension(R.dimen.horizontal_margin);

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
    //        boolean bool = Res.getResourceBoolean(R.bool.some_boolean);
    //
    //        // Assert
    //        boolean expectedBool = getApplicationResources().getBoolean(R.bool.some_boolean);
    //        assertEquals(expectedBool, bool);
    //    }

    @Test
    public void getResourceColor_ShouldContainSameColorAsGetColorFromApplicationContext() {
        // Arrange

        // Act
        int actualColorInt = Res.getColor(R.color.colorAccent);

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
