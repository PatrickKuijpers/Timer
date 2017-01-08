package nl.tcilegnar.timer;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    protected static Resources getAppResources() {
        return context.getResources();
    }

    public static String getResourceString(@StringRes int resId) {
        return getAppResources().getString(resId);
    }

    public static Integer getResourceInteger(@IntegerRes int resId) {
        return getAppResources().getInteger(resId);
    }

    public static int getResourceDimension(@DimenRes int resId) {
        return (int) (getAppResources().getDimension(resId) / getAppResources().getDisplayMetrics().density);
    }

    public static boolean getResourceBoolean(@BoolRes int resId) {
        return getAppResources().getBoolean(resId);
    }

    public static
    @ColorInt
    int getResourceColor(@ColorRes int resId) {
        return ContextCompat.getColor(context, resId);
    }

    /** stackoverflow.com/questions/37998266/android-studio-many-error-could-not-find-class-android-xxx#38666791 */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        try {
            MultiDex.install(this);
        } catch (RuntimeException multiDexException) {
            // Work around Robolectric causing multi dex installation to fail, see
            // https://code.google.com/p/android/issues/detail?id=82007.
            boolean isUnderUnitTest;

            try {
                Class<?> robolectric = Class.forName("org.robolectric.Robolectric");
                isUnderUnitTest = (robolectric != null);
            } catch (ClassNotFoundException e) {
                isUnderUnitTest = false;
            }

            if (!isUnderUnitTest) {
                // Re-throw if this does not seem to be triggered by Robolectric.
                throw multiDexException;
            }
        }
    }
}
