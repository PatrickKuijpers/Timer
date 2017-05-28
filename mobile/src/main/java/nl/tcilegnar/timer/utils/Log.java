package nl.tcilegnar.timer.utils;

import nl.tcilegnar.timer.BuildConfig;

public class Log {
    private static final String BASE_LOGTAG = "TimerLog";

    public enum Type {
        NAVIGATION, STORAGE, TEST
    }

    public static void v(Type type, String msg) {
        v(type.name(), msg);
    }

    public static void v(String logTag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(finalLogTag(logTag), msg);
        }
    }

    public static void d(Type type, String msg) {
        d(type.name(), msg);
    }

    public static void d(String logTag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(finalLogTag(logTag), msg);
        }
    }

    public static void i(Type type, String msg) {
        i(type.name(), msg);
    }

    public static void i(String logTag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.i(finalLogTag(logTag), msg);
        }
    }

    public static void w(Type type, String msg) {
        w(type.name(), msg);
    }

    public static void w(String logTag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.w(finalLogTag(logTag), msg);
        }
    }

    public static void e(Type type, String msg) {
        e(type.name(), msg);
    }

    public static void e(String logTag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(finalLogTag(logTag), msg);
        }
    }

    public static void wtf(Type type, String msg) {
        wtf(type.name(), msg);
    }

    public static void wtf(String logTag, String msg) {
        if (BuildConfig.DEBUG) {
            android.util.Log.wtf(finalLogTag(logTag), msg);
        }
    }

    private static String finalLogTag(String logTag) {
        return BASE_LOGTAG + " " + logTag;
    }
}
