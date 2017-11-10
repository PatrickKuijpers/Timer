package nl.tcilegnar.timer.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Calendar;

import nl.tcilegnar.timer.fragments.WeekOverviewFragment;
import nl.tcilegnar.timer.fragments.WeekOverviewFragment.OnDayClickListener;
import nl.tcilegnar.timer.utils.TimerCalendar;

import static nl.tcilegnar.timer.fragments.WeekOverviewFragment.Args.DATE_FROM_WEEK;

public class WeekOverviewActivity extends BaseActivity {

    @NonNull
    protected Fragment getInitialFragment() {
        Calendar dateFromWeek = getDateFromWeek();
        WeekOverviewFragment fragment = WeekOverviewFragment.newInstance(dateFromWeek);
        fragment.setDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClicked(Calendar dateOfDay) {
                startDayEditorActivity(dateOfDay);
            }
        });
        return fragment;
    }

    @NonNull
    private Calendar getDateFromWeek() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long dateInMillis = extras.getLong(DATE_FROM_WEEK.name());
            return TimerCalendar.getCalendarInMillis(dateInMillis);
        } else {
            return TimerCalendar.getCurrent();
        }
    }
}
