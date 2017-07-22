package nl.tcilegnar.timer.utils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/** Most methods are meant to make the use of {@link Calendar} easier, until Android supports Java8 */
public class TimerCalendarUtil {
    public static boolean isToday(Calendar date) {
        return areSameDay(date, TimerCalendar.getCurrent());
    }

    public static boolean areSameDay(Calendar firstCal, Calendar secondCal) {
        boolean isSameYear = firstCal.get(Calendar.YEAR) == secondCal.get(Calendar.YEAR);
        boolean isSameDay = firstCal.get(Calendar.DAY_OF_YEAR) == secondCal.get(Calendar.DAY_OF_YEAR);
        return isSameYear && isSameDay;
    }

    public static int getDateDiff(Calendar cal1, Calendar cal2, TimeUnit timeUnit) {
        long diffInMillis = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        return (int) timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static String getReadableTimeStringHoursAndMinutes(int timeInMinutes) {
        int hours = timeInMinutes / 60;
        int minutes = timeInMinutes % 60;
        return String.format(MyLocale.getLocaleForTranslationAndSigns(), "%d:%02d", hours, minutes);
    }

    public static String getReadableTimeStringHoursAndMinutesLetters(int timeInMinutes) {
        int hours = timeInMinutes / 60;
        int minutes = timeInMinutes % 60;
        return String.format(MyLocale.getLocaleForTranslationAndSigns(), "%dh %02dm", hours, minutes);
    }
}
