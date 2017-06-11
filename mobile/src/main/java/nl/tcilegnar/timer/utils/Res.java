package nl.tcilegnar.timer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.BoolRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import nl.tcilegnar.timer.App;

public class Res {
    public static String getString(@StringRes int resId) {
        return getApplicationResources().getString(resId);
    }

    public static int getInt(@IntegerRes int resId) {
        return getApplicationResources().getInteger(resId);
    }

    public static int getDimension(@DimenRes int resId) {
        return (int) (getApplicationResources().getDimension(resId) / getApplicationResources().getDisplayMetrics()
                .density);
    }

    public static boolean getBoolean(@BoolRes int resId) {
        return getApplicationResources().getBoolean(resId);
    }

    public static
    @ColorInt
    int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    protected static Resources getApplicationResources() {
        return getContext().getResources();
    }

    private static Context getContext() {
        return App.getContext();
    }
}
