package nl.tcilegnar.timer.utils;

import java.util.Calendar;

/** This class is meant to make the use of {@link Calendar} easier, until Android supports Java8 */
public class TimerCalendar {
    public static Calendar getCurrent() {
        return Calendar.getInstance(MyLocale.getLocale());
    }

    public static Calendar getCurrentDate() {
        return getCalendarWithTime(getCurrent(), 0, 0);
    }

    public static Calendar getCalendarInMillis(long timeInMillis) {
        Calendar cal = getCurrent();
        cal.setTimeInMillis(timeInMillis);
        return cal;
    }

    /** A copy with same timezone and time in millis. Because the normal copy method doesn't work as expected */
    private static Calendar getCopyOfCalendar(Calendar cal) {
        return getCalendarInMillis(cal.getTimeInMillis());
    }

    public static Calendar getCalendarWithDate(int year, int month, int dayOfMonth) {
        Calendar cal = getCalendarCurrentDateWithTime(0, 0);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return cal;
    }

    public static Calendar getCalendarCurrentDateWithTime(int hour, int minute) {
        return getCalendarWithTime(getCurrent(), hour, minute);
    }

    public static Calendar getCalendarWithTime(Calendar originalCal, int hour, int minute) {
        Calendar cal = getCopyOfCalendar(originalCal);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar getCalendarWithCurrentTime(Calendar originalCal) {
        Calendar currentDay = getCurrent();
        return getCalendarWithTime(originalCal, currentDay.get(Calendar.HOUR_OF_DAY), currentDay.get(Calendar.MINUTE));
    }
}
