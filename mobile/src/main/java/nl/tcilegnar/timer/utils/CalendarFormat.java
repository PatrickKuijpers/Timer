package nl.tcilegnar.timer.utils;

import java.util.Locale;

public class CalendarFormat {
    private static final Locale LOCALE = Locale.GERMAN;

    public static String get24hTimeString(int hour, int minute) {
        String hourText = String.format(LOCALE, "%02d", hour);
        String minuteText = String.format(LOCALE, "%02d", minute);
        return hourText + ":" + minuteText;
    }
}
