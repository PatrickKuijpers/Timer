package nl.tcilegnar.timer.activities;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.UpdateManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Calendar;

import nl.tcilegnar.timer.fragments.DayEditorFragment;
import nl.tcilegnar.timer.fragments.DayEditorFragment.SaveListener;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.MyBuildConfig;
import nl.tcilegnar.timer.utils.MyProperties;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.storage.Storage;

import static nl.tcilegnar.timer.fragments.DayEditorFragment.Args.DAY_DATE;
import static nl.tcilegnar.timer.utils.storage.Storage.NO_ACTIVE_DAY_EDITOR;

public class DayEditorActivity extends BaseActivity {
    MyBuildConfig myBuildConfig = new MyBuildConfig();

    @NonNull
    protected Fragment getInitialFragment() {
        Calendar dayDate = getDayDate();
        DayEditorFragment dayEditorFragment = DayEditorFragment.newInstance(dayDate);
        dayEditorFragment.setSaveListener(new SaveListener() {
            @Override
            public void onSaveSuccessful(Calendar someDateFromWeek) {
                startWeekOverviewActivity(someDateFromWeek);
            }
        });
        return dayEditorFragment;
    }

    @NonNull
    private Calendar getDayDate() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long dateInMillis = extras.getLong(DAY_DATE.name());
            return TimerCalendar.getCalendarInMillis(dateInMillis);
        } else {
            return getDayFromLastActiveDayEditor();
        }
    }

    public static Calendar getDayFromLastActiveDayEditor() {
        Storage storage = new Storage();
        if (storage.loadActiveDayEditorState() != NO_ACTIVE_DAY_EDITOR) {
            return storage.loadTodayEditorCurrentDate();
        } else {
            // No day editor active = no time set: assume you'd like to start over with a new day instead of retrieving
            return TimerCalendar.getCurrentDateMidnight();
        }
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
                Log.i(LOGTAG, "Helaas geen crash reports & andere handige tools van HockeyApp");
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
