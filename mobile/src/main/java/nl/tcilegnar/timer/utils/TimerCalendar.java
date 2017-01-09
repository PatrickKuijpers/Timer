package nl.tcilegnar.timer.utils;

import java.util.Calendar;
import java.util.Locale;

public class TimerCalendar {
    private static final Locale LOCALE = Locale.GERMANY;

    public static Calendar getCurrentDate() {
        return Calendar.getInstance(LOCALE);
    }
}
