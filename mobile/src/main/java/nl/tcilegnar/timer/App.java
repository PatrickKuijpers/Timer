package nl.tcilegnar.timer;

import com.orm.SugarApp;

import android.content.Context;

public class App extends SugarApp {
    private static Context context;

    @Override
    public void onCreate() {
        if (callSuper()) {
            super.onCreate();
        }
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onTerminate() {
        if (callSuper()) {
            super.onTerminate();
        }
    }

    protected boolean callSuper() {
        return true;
    }
}
