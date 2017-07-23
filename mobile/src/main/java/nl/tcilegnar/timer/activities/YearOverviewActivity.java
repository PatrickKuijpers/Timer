package nl.tcilegnar.timer.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import nl.tcilegnar.timer.fragments.YearOverviewFragment;
import nl.tcilegnar.timer.utils.TimerCalendar;

public class YearOverviewActivity extends BaseActivity {

    @NonNull
    protected Fragment getInitialFragment() {
        return YearOverviewFragment.newInstance(TimerCalendar.getCurrent());
    }
}
