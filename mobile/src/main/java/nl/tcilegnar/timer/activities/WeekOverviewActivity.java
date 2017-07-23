package nl.tcilegnar.timer.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import nl.tcilegnar.timer.fragments.WeekOverviewFragment;
import nl.tcilegnar.timer.utils.TimerCalendar;

public class WeekOverviewActivity extends BaseActivity {

    @NonNull
    protected Fragment getInitialFragment() {
        return WeekOverviewFragment.newInstance(TimerCalendar.getCurrent());
    }
}
