package nl.tcilegnar.timer.utils;

import java.util.Calendar;
import java.util.Locale;

public class TimerCalendar {
    private static final Locale LOCALE = Locale.GERMANY;

    public static Calendar getCurrentDate() {
        return Calendar.getInstance(LOCALE);
    }

    public static Calendar getCurrentDateWithTime(int hour, int minute) {
        Calendar currentDate = getCurrentDate();
        currentDate.set(Calendar.HOUR_OF_DAY, hour);
        currentDate.set(Calendar.MINUTE, minute);
        return currentDate;
    }
}
