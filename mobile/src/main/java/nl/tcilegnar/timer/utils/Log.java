package nl.tcilegnar.timer.utils;

import nl.tcilegnar.timer.R;

/**
 * Log bouwt een android log op door een default tag ({@link #BASE_LOGTAG}) en evt extra tags in de message om logs te
 * kunnen filteren. Deze tags zijn ofwel een string die meegegeven worden als logTag (zoals bij de Android.util.Log) of
 * in plaats daarvan 'this' om de classnaam te kunnen loggen. Daarnaast kunnen optioneel categories worden meegegeven
 * (zie {@link Cat}), waarop gefilterd kan worden (Bijv. filteren op "_TEST_").
 */
public class Log {
    private static final String BASE_LOGTAG = "Timer";
    private static final String CAT_FILTER_DIVIDER = "_";
    private static final String SPACE = Res.getString(R.string.space);

    /**
     * The type (/ category) can be used as tag, or supplemented to a tag. With this additional info logs can be easily
     * filtered (eg. a filter for "_TEST_")
     */
    public enum Cat {
        TEST, PREFS, DATABASE, NAVIGATION, TIMER
    }

    public static void v(Object someClass, String msg, Cat... categories) {
        v(getClassName(someClass), msg, categories);
    }

    public static void v(String logTag, String msg, Cat... categories) {
        if (LogPrefs.shouldLog()) {
            android.util.Log.v(BASE_LOGTAG, getMessage(msg, logTag, categories));
        }
    }

    public static void d(Object someClass, String msg, Cat... categories) {
        d(getClassName(someClass), msg, categories);
    }

    public static void d(String logTag, String msg, Cat... categories) {
        if (LogPrefs.shouldLog()) {
            android.util.Log.d(BASE_LOGTAG, getMessage(msg, logTag, categories));
        }
    }

    public static void i(Object someClass, String msg, Cat... categories) {
        i(getClassName(someClass), msg, categories);
    }

    public static void i(String logTag, String msg, Cat... categories) {
        if (LogPrefs.shouldLog()) {
            android.util.Log.i(BASE_LOGTAG, getMessage(msg, logTag, categories));
        }
    }

    public static void w(Object someClass, String msg, Cat... categories) {
        w(getClassName(someClass), msg, categories);
    }

    public static void w(String logTag, String msg, Cat... categories) {
        if (LogPrefs.shouldLog()) {
            android.util.Log.w(BASE_LOGTAG, getMessage(msg, logTag, categories));
        }
    }

    public static void e(Object someClass, String msg, Cat... categories) {
        e(getClassName(someClass), msg, categories);
    }

    public static void e(String logTag, String msg, Cat... categories) {
        if (LogPrefs.shouldLog()) {
            android.util.Log.e(BASE_LOGTAG, getMessage(msg, logTag, categories));
        }
    }

    public static void wtf(Object someClass, String msg, Cat... categories) {
        wtf(getClassName(someClass), msg, categories);
    }

    public static void wtf(String logTag, String msg, Cat... categories) {
        if (LogPrefs.shouldLog()) {
            android.util.Log.wtf(BASE_LOGTAG, getMessage(msg, logTag, categories));
        }
    }

    private static String getClassName(Object someClass) {
        return someClass.getClass().getSimpleName();
    }

    private static String getMessage(String msg, String logTag, Cat[] categories) {
        return getTag(logTag, categories) + ": " + msg;
    }

    public static String getTag(Object someClass, Cat... categories) {
        return getTag(getClassName(someClass), categories);
    }

    public static String getTag(String logTag, Cat... categories) {
        return getTag(categories) + getTagSuffix(logTag);
    }

    public static String getTag(Cat... categories) {
        StringBuilder logTag = new StringBuilder();
        for (Cat category : categories) {
            logTag.append(CAT_FILTER_DIVIDER);
            logTag.append(category.toString());
        }
        if (categories.length != 0) {
            logTag.append(CAT_FILTER_DIVIDER);
            logTag.append(SPACE);
        }
        return logTag.toString();
    }

    private static String getTagSuffix(String logTag) {
        StringBuilder returnTag = new StringBuilder();
        if (logTag != null) {
            returnTag.append(logTag);
        }
        return returnTag.toString();
    }
}