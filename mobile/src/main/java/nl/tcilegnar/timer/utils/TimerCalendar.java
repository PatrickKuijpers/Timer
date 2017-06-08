package nl.tcilegnar.timer.utils;

import java.util.Calendar;

public class TimerCalendar {
    public static Calendar getCurrentDay() {
        return Calendar.getInstance(MyLocale.getLocale());
    }

    public static Calendar getCalendarInMillis(long timeInMillis) {
        Calendar cal = getCurrentDay();
        cal.setTimeInMillis(timeInMillis);
        return cal;
    }

    public static Calendar getCalendarCurrentDayWithTime(int hour, int minute) {
        Calendar currentDate = getCurrentDay();
        return getCalendarWithTime(currentDate, hour, minute);
    }

    public static Calendar getCalendarWithTime(Calendar cal, int hour, int minute) {
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal;
    }
}
