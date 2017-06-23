package nl.tcilegnar.timer.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import nl.tcilegnar.timer.App;
import nl.tcilegnar.timer.R;

public class AppData {
    public static String getAppName() {
        return Res.getString(R.string.app_name);
    }

    public static String getAppVersionName() {
        Context context = App.getContext();
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return context.getString(R.string.version_unknown);
        }
    }
}
