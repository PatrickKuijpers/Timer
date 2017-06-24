package nl.tcilegnar.timer.fragments;

import com.orm.query.Condition;
import com.orm.query.Select;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.WeekOverviewAdapter;
import nl.tcilegnar.timer.fragments.dialogs.LoadErrorDialog;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.AppData;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.Res;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;

public class WeekOverviewFragment extends Fragment {
    private final String TAG = Log.getTag(this);
    private final Calendar currentDate = TimerCalendar.getCurrent();

    private TextView weekNumberValueView;
    private TextView totalValueLabelView;
    private TextView totalValueView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        weekNumberValueView = (TextView) view.findViewById(R.id.week_number_value);
        totalValueLabelView = (TextView) view.findViewById(R.id.total_value_label);
        totalValueView = (TextView) view.findViewById(R.id.total_value);

        setWeekNumber(currentDate);
        try {
            List<CurrentDayMillis> currentDayMillisOfWeek = getCurrentDayMillisOfWeek(currentDate);

            setTotalTime(currentDayMillisOfWeek);
            initWeekOverviewList(view, currentDayMillisOfWeek);
        } catch (Exception e) {
            e.printStackTrace();
            new LoadErrorDialog(String.format(Res.getString(R.string.error_message_dialog_load_weeknumber),
                    currentDate.get(Calendar.WEEK_OF_YEAR)));
        }
        setVersionNumber(view);
    }

    private List<CurrentDayMillis> getCurrentDayMillisOfWeek(Calendar date) {
        String startOfThisWeekMillis = String.valueOf(TimerCalendar.getFirstDayOfWeek(date).getTimeInMillis());
        String startOfNextWeekMillis = String.valueOf(TimerCalendar.getFirstDayOfNextWeek(date).getTimeInMillis());

        Condition firstDayOfWeek = Condition.prop("DAY_IN_MILLIS").eq(startOfThisWeekMillis);
        Condition[] allDaysOfWeekExceptFirstDay = {Condition.prop("DAY_IN_MILLIS").gt(startOfThisWeekMillis),
                Condition.prop("DAY_IN_MILLIS").lt(startOfNextWeekMillis)};
        
        List<CurrentDayMillis> currentDayMillisOfWeek = Select.from(CurrentDayMillis.class).where
                (allDaysOfWeekExceptFirstDay).or(firstDayOfWeek).orderBy("DAY_IN_MILLIS").list();
        Log.i(TAG, "listAll: " + currentDayMillisOfWeek.size() + " entries found");
        for (CurrentDayMillis currentDayMillis : currentDayMillisOfWeek) {
            Log.v(TAG, "ID: " + currentDayMillis.getId() + " - " + currentDayMillis.toString());
        }
        return currentDayMillisOfWeek;
    }

    private void setWeekNumber(Calendar currentDate) {
        int weekNumber = currentDate.get(Calendar.WEEK_OF_YEAR);
        weekNumberValueView.setText(String.valueOf(weekNumber));
    }

    private void setTotalTime(List<CurrentDayMillis> currentDayMillisOfWeek) {
        int totalTimeInMinutes = getTotalTimeOfWeekInMinutes(currentDayMillisOfWeek);
        String timeString = getTotalTimeString(totalTimeInMinutes);
        if (!timeString.isEmpty()) {
            totalValueView.setText(timeString);
            totalValueView.setVisibility(View.VISIBLE);
            totalValueLabelView.setVisibility(View.VISIBLE);
        } else {
            totalValueView.setVisibility(View.GONE);
            totalValueLabelView.setVisibility(View.GONE);
        }
    }

    private int getTotalTimeOfWeekInMinutes(List<CurrentDayMillis> currentDayMillisOfWeek) {
        int totalTimeInMinutes = 0;
        for (CurrentDayMillis currentDayMillis : currentDayMillisOfWeek) {
            totalTimeInMinutes += currentDayMillis.getTotalTimeInMinutes();
        }
        return totalTimeInMinutes;
    }

    private String getTotalTimeString(int totalTimeInMinutes) {
        return TimerCalendarUtil.getReadableTimeStringHoursAndMinutes(totalTimeInMinutes);
    }

    private void initWeekOverviewList(View view, List<CurrentDayMillis> currentDayMillisOfWeek) {
        ListView weekOverviewList = (ListView) view.findViewById(R.id.week_overview_list);
        WeekOverviewAdapter weekOverviewAdapter = new WeekOverviewAdapter(getActivity(), currentDayMillisOfWeek);
        weekOverviewList.setAdapter(weekOverviewAdapter);
    }

    public void setVersionNumber(View view) {
        TextView versionNrView = (TextView) view.findViewById(R.id.version_nr);
        versionNrView.setText(AppData.getAppVersionName());
    }
}
