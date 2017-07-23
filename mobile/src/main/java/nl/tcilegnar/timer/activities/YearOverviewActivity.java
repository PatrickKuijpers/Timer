package nl.tcilegnar.timer.activities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Calendar;

import nl.tcilegnar.timer.fragments.YearOverviewFragment;
import nl.tcilegnar.timer.fragments.YearOverviewFragment.OnWeekClickListener;
import nl.tcilegnar.timer.utils.TimerCalendar;

public class YearOverviewActivity extends BaseActivity implements OnWeekClickListener {

    @NonNull
    protected Fragment getInitialFragment() {
        YearOverviewFragment fragment = YearOverviewFragment.newInstance(TimerCalendar.getCurrent());
        fragment.setWeekClickListener(this);
        return fragment;
    }

    @Override
    public void onWeekClicked(Calendar someDateFromWeek) {
        startWeekOverviewActivity(someDateFromWeek);
    }
}
