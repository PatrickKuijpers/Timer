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
import nl.tcilegnar.timer.adapters.YearOverviewAdapter;
import nl.tcilegnar.timer.dialogs.DatePickerFragment;
import nl.tcilegnar.timer.fragments.dialogs.LoadErrorDialog;
import nl.tcilegnar.timer.models.Year;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.Res;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;

import static nl.tcilegnar.timer.utils.TimerCalendar.getCurrentDate;

public class YearOverviewFragment extends Fragment {
    private final String TAG = Log.getTag(this);
    private static final String YEAR_PICKER_DIALOG_TAG = "YEAR_PICKER_DIALOG_TAG";

    private Calendar dateFromYear = TimerCalendar.getCurrent();

    private TextView yearValueView;
    private TextView totalValueLabelView;
    private TextView totalValueView;
    private ListView yearOverviewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_year_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initListeners();
    }

    private void initViews(View view) {
        yearValueView = (TextView) view.findViewById(R.id.year_value);
        totalValueLabelView = (TextView) view.findViewById(R.id.total_value_label);
        totalValueView = (TextView) view.findViewById(R.id.total_value);
        yearOverviewList = (ListView) view.findViewById(R.id.year_overview_list);

        updateYearValues();
    }

    private void updateYearValues() {
        try {
            List<CurrentDayMillis> currentDayMillisOfYear = getCurrentDayMillisOfYear(dateFromYear);
            Year year = new Year(dateFromYear, currentDayMillisOfYear);
            setYearNumber(year);
            setTotalTime(currentDayMillisOfYear); // TODO
            updateYearOverviewList(year);
        } catch (Exception e) {
            e.printStackTrace();
            new LoadErrorDialog(String.format(Res.getString(R.string.error_message_dialog_load_weeknumber),
                    dateFromYear.get(Calendar.WEEK_OF_YEAR)));
        }
    }

    private List<CurrentDayMillis> getCurrentDayMillisOfYear(Calendar date) {
        String startOfThisYearMillis = String.valueOf(TimerCalendar.getFirstDayOfYear(date).getTimeInMillis());
        String startOfNextYearMillis = String.valueOf(TimerCalendar.getFirstDayOfNextYear(date).getTimeInMillis());

        Condition firstDayOfYear = Condition.prop("DAY_IN_MILLIS").eq(startOfThisYearMillis);
        Condition[] allDaysOfYearExceptFirstDay = {Condition.prop("DAY_IN_MILLIS").gt(startOfThisYearMillis),
                Condition.prop("DAY_IN_MILLIS").lt(startOfNextYearMillis)};

        List<CurrentDayMillis> currentDayMillisOfYear = Select.from(CurrentDayMillis.class).where
                (allDaysOfYearExceptFirstDay).or(firstDayOfYear).orderBy("DAY_IN_MILLIS").list();
        Log.i(TAG, currentDayMillisOfYear.size() + " entries found for this year");
        for (CurrentDayMillis currentDayMillis : currentDayMillisOfYear) {
            Log.v(TAG, currentDayMillis.toString());
        }
        return currentDayMillisOfYear;
    }

    private void setYearNumber(Year year) {
        String yearNumberText = String.valueOf(year.getYearNumber());
        yearValueView.setText(yearNumberText);
    }

    private void setTotalTime(List<CurrentDayMillis> currentDayMillisOfYear) {
        int totalTimeInMinutes = getTotalTimeOfYearInMinutes(currentDayMillisOfYear);
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

    private int getTotalTimeOfYearInMinutes(List<CurrentDayMillis> currentDayMillisOfYear) {
        int totalTimeInMinutes = 0;
        for (CurrentDayMillis currentDayMillis : currentDayMillisOfYear) {
            totalTimeInMinutes += currentDayMillis.getTotalTimeInMinutes();
        }
        totalTimeInMinutes += 40000;
        return totalTimeInMinutes;
    }

    private String getTotalTimeString(int totalTimeInMinutes) {
        return TimerCalendarUtil.getReadableTimeStringHoursAndMinutes(totalTimeInMinutes);
    }

    private void updateYearOverviewList(Year year) {
        YearOverviewAdapter yearOverviewAdapter = new YearOverviewAdapter(getActivity(), year);
        yearOverviewList.setAdapter(yearOverviewAdapter);
    }

    private void initListeners() {
        yearValueView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearSelectionDialog();
            }
        });
    }

    public void showYearSelectionDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setNewDate(TimerCalendar.getCalendarWithDate(year, month, dayOfMonth));
            }
        });
        datePickerFragment.show(getActivity().getFragmentManager(), YEAR_PICKER_DIALOG_TAG, getCurrentDate());
    }

    public void setNewDate(Calendar dateFromYear) {
        this.dateFromYear = dateFromYear;
        updateYearValues();
    }
}
