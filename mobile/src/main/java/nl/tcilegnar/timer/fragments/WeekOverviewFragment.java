package nl.tcilegnar.timer.fragments;

import com.orm.query.Condition;
import com.orm.query.Select;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.WeekOverviewAdapter;
import nl.tcilegnar.timer.dialogs.DatePickerFragment;
import nl.tcilegnar.timer.fragments.dialogs.LoadErrorDialog;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.AppData;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.Res;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;

import static nl.tcilegnar.timer.utils.TimerCalendar.getCurrentDate;

public class WeekOverviewFragment extends Fragment {
    private final String TAG = Log.getTag(this);
    private static final String DATE_PICKER_DIALOG_TAG = "DATE_PICKER_DIALOG_TAG";

    private Calendar dateFromWeek = TimerCalendar.getCurrent();

    private TextView weekNumberValueView;
    private TextView totalValueLabelView;
    private TextView totalValueView;
    private ListView weekOverviewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initListeners();
    }

    private void initViews(View view) {
        weekNumberValueView = (TextView) view.findViewById(R.id.week_number_value);
        totalValueLabelView = (TextView) view.findViewById(R.id.total_value_label);
        totalValueView = (TextView) view.findViewById(R.id.total_value);
        weekOverviewList = (ListView) view.findViewById(R.id.week_overview_list);

        updateWeekValues();
        setVersionNumber(view);
    }

    private void updateWeekValues() {
        setWeekNumber(dateFromWeek);
        try {
            List<CurrentDayMillis> currentDayMillisOfWeek = getCurrentDayMillisOfWeek(dateFromWeek);
            setTotalTime(currentDayMillisOfWeek);
            updateWeekOverviewList(currentDayMillisOfWeek);
        } catch (Exception e) {
            e.printStackTrace();
            new LoadErrorDialog(String.format(Res.getString(R.string.error_message_dialog_load_weeknumber),
                    dateFromWeek.get(Calendar.WEEK_OF_YEAR)));
        }
    }

    private List<CurrentDayMillis> getCurrentDayMillisOfWeek(Calendar date) {
        String startOfThisWeekMillis = String.valueOf(TimerCalendar.getFirstDayOfWeek(date).getTimeInMillis());
        String startOfNextWeekMillis = String.valueOf(TimerCalendar.getFirstDayOfNextWeek(date).getTimeInMillis());

        Condition firstDayOfWeek = Condition.prop("DAY_IN_MILLIS").eq(startOfThisWeekMillis);
        Condition[] allDaysOfWeekExceptFirstDay = {Condition.prop("DAY_IN_MILLIS").gt(startOfThisWeekMillis),
                Condition.prop("DAY_IN_MILLIS").lt(startOfNextWeekMillis)};

        List<CurrentDayMillis> currentDayMillisOfWeek = Select.from(CurrentDayMillis.class).where
                (allDaysOfWeekExceptFirstDay).or(firstDayOfWeek).orderBy("DAY_IN_MILLIS").list();
        Log.i(TAG, currentDayMillisOfWeek.size() + " entries found for this week");
        for (CurrentDayMillis currentDayMillis : currentDayMillisOfWeek) {
            Log.v(TAG, currentDayMillis.toString());
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

    private void updateWeekOverviewList(List<CurrentDayMillis> currentDayMillisOfWeek) {
        WeekOverviewAdapter weekOverviewAdapter = new WeekOverviewAdapter(getActivity(), currentDayMillisOfWeek);
        weekOverviewList.setAdapter(weekOverviewAdapter);
    }

    public void setVersionNumber(View view) {
        TextView versionNrView = (TextView) view.findViewById(R.id.version_nr);
        versionNrView.setText(AppData.getAppVersionName());
    }

    private void initListeners() {
        weekNumberValueView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setNewDate(TimerCalendar.getCalendarWithDate(year, month, dayOfMonth));
            }
        });
        datePickerFragment.show(getActivity().getFragmentManager(), DATE_PICKER_DIALOG_TAG, getCurrentDate());
    }

    public void setNewDate(Calendar dateFromWeek) {
        this.dateFromWeek = dateFromWeek;
        updateWeekValues();
    }
}
