package nl.tcilegnar.timer.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.support.annotation.NonNull;

import java.util.Calendar;

import nl.tcilegnar.timer.BuildConfig;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TimerCalendarTest {
    private static final long MON_17_07_2017_MIDNIGHT_NL = 1500242400000L;
    private static final long SAT_22_07_2017_MIDNIGHT_NL = 1500674400000L;
    private static final long MON_24_07_2017_MIDNIGHT_NL = 1500847200000L;

    @Test
    public void getFirstDayOfWeek() {
        // Arrange
        Calendar currentTime = getCurrentTimeDummy();

        // Act
        Calendar firstDayOfWeek = TimerCalendar.getFirstDayOfWeek(currentTime);

        // Assert
        assertEquals(MON_17_07_2017_MIDNIGHT_NL, firstDayOfWeek.getTimeInMillis());
    }

    @Test
    public void getFirstDayOfNextWeek() {
        // Arrange
        Calendar currentTime = getCurrentTimeDummy();

        // Act
        Calendar firstDayOfNextWeek = TimerCalendar.getFirstDayOfNextWeek(currentTime);

        // Assert
        assertEquals(MON_24_07_2017_MIDNIGHT_NL, firstDayOfNextWeek.getTimeInMillis());
    }

    @NonNull
    private Calendar getCurrentTimeDummy() {
        Calendar now = TimerCalendar.getCurrent();
        now.setTimeInMillis(SAT_22_07_2017_MIDNIGHT_NL);
        return now;
    }
}
