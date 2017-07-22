package nl.tcilegnar.timer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatter {
    public static final String TIME_SHORT = "HH:mm";
    public static final String TIME_WITH_SECONDS = TIME_SHORT + ":ss";
    public static final String TIME_WITH_MILLISECONDS = TIME_WITH_SECONDS + ":SSS";

    public static final String DAY_OF_WEEK_SHORT = "EEE";
    public static final String DAY_OF_WEEK_LONG = "EEEE";

    public static final String DATE_FORMAT_SPACES_1_JANUARY = "d MMMM";
    public static final String DATE_FORMAT_DASHES_1_JAN = "d-MMM";
    public static final String DATE_FORMAT_SPACES_01_JAN_2000 = "dd MMM yyyy";
    public static final String DATE_FORMAT_SPACES_1_JAN_2000 = "d MMM yyyy";
    public static final String DATE_FORMAT_DASHES_2000_01_01 = "yyyy-MM-dd";

    public static String format(Calendar calendar, String template) {
        return format(calendar.getTime(), template);
    }

    public static String format(Date date, String template) {
        return new SimpleDateFormat(template, MyLocale.getLocaleForTranslationAndSigns()).format(date);
    }
}

