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
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar getCalendarWithCurrentTime(Calendar cal) {
        Calendar currentDay = getCurrentDay();
        return getCalendarWithTime(cal, currentDay.get(Calendar.HOUR_OF_DAY), currentDay.get(Calendar.MINUTE));
    }
}
