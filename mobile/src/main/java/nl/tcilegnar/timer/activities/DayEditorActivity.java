package nl.tcilegnar.timer.activities;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import nl.tcilegnar.timer.fragments.DayEditorFragment;
import nl.tcilegnar.timer.fragments.DayEditorFragment.SaveListener;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.MyBuildConfig;
import nl.tcilegnar.timer.utils.MyProperties;

public class DayEditorActivity extends BaseActivity {
    MyBuildConfig myBuildConfig = new MyBuildConfig();

    @NonNull
    protected Fragment getInitialFragment() {
        DayEditorFragment dayEditorFragment = new DayEditorFragment();
        dayEditorFragment.setSaveListener(new SaveListener() {
            @Override
            public void onSaveSuccessful() {
                startWeekOverviewActivity();
            }
        });
        return dayEditorFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerHockeyAppManagers();
    }

    private void registerHockeyAppManagers() {
        if (!myBuildConfig.isProduction()) {
            try {
                String hockeyAppId = MyProperties.getHockeyAppId();
                checkForCrashes(hockeyAppId);
                checkForUpdates(hockeyAppId);
            } catch (Exception e) {
                Log.w(LOGTAG, "Helaas geen crash reports & andere handige tools van HockeyApp");
                e.printStackTrace();
            }
        }
    }

    private void checkForCrashes(String hockeyAppId) {
        CrashManager.register(this, hockeyAppId, new CrashManagerListener() {
            public boolean shouldAutoUploadCrashes() {
                return true;
            }
        });
    }

    private void checkForUpdates(String hockeyAppId) {
        UpdateManager.register(this, hockeyAppId, true);
    }

    private void unregisterHockeyAppManagers() {
        if (!myBuildConfig.isProduction()) {
            UpdateManager.unregister();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterHockeyAppManagers();
    }
}
