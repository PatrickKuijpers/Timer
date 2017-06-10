package nl.tcilegnar.timer.utils;

import java.util.Calendar;

public class TimerCalendarUtil {

    public static boolean areSameDay(Calendar firstCal, Calendar secondCal) {
        boolean isSameYear = firstCal.get(Calendar.YEAR) == secondCal.get(Calendar.YEAR);
        boolean isSameDay = firstCal.get(Calendar.DAY_OF_YEAR) == secondCal.get(Calendar.DAY_OF_YEAR);
        return isSameYear && isSameDay;
    }
}