package nl.tcilegnar.timer.utils;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/** This class is meant to make the use of {@link Calendar} easier, until Android supports Java8 */
public class TimerCalendar {
    public static Calendar getCurrent() {
        return Calendar.getInstance(MyLocale.getLocale());
    }

    public static Calendar getCurrentDateMidnight() {
        return getCalendarWithTime(getCurrent(), 0, 0);
    }

    public static Calendar getCalendarInMillis(long timeInMillis) {
        Calendar cal = getCurrent();
        cal.setTimeInMillis(timeInMillis);
        return cal;
    }

    /** A copy with same timezone and time in millis. Because the normal copy method doesn't work as expected */
    public static Calendar getCopyOfCalendar(Calendar cal) {
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

    // TODO (PK): http://stackoverflow.com/questions/43321620/get-a-user-visible-text-for-the-day-of-the-week-correct
    // -language-using-the-day
    public static String getDayOfWeekTextShort(int dayOfWeek) {
        return DateFormatSymbols.getInstance(MyLocale.getLocaleForTranslationAndSigns()).getShortWeekdays()[dayOfWeek];
    }

    public static String getDayOfWeekTextLong(int dayOfWeek) {
        return DateFormatSymbols.getInstance(MyLocale.getLocaleForTranslationAndSigns()).getWeekdays()[dayOfWeek];
    }

    public static Calendar getFirstDayOfWeek(Calendar currentDate) {
        Calendar cal = TimerCalendar.getCopyOfCalendar(currentDate);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal = getCalendarWithTime(cal, 0, 0);
        return cal;
    }

    public static Calendar getLastDayOfWeek(Calendar currentDate) {
        Calendar cal = getFirstDayOfWeek(currentDate);
        cal.add(Calendar.DAY_OF_YEAR, 6);
        return cal;
    }

    public static Calendar getFirstDayOfNextWeek(Calendar currentDate) {
        Calendar cal = getFirstDayOfWeek(currentDate);
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        return cal;
    }

    public static Calendar getFirstDayOfYear(Calendar currentDate) {
        Calendar cal = TimerCalendar.getCopyOfCalendar(currentDate);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal = getCalendarWithTime(cal, 0, 0);
        return cal;
    }

    public static Calendar getFirstDayOfNextYear(Calendar currentDate) {
        Calendar cal = getFirstDayOfYear(currentDate);
        cal.add(Calendar.YEAR, 1);
        return cal;
    }
}
