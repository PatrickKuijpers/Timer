package nl.tcilegnar.timer.fragments;

import com.orm.query.Condition;
import com.orm.query.Select;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.WeekOverviewAdapter;
import nl.tcilegnar.timer.dialogs.DatePickerFragment;
import nl.tcilegnar.timer.fragments.dialogs.LoadErrorDialog;
import nl.tcilegnar.timer.models.Week;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.AppData;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.Res;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;
import nl.tcilegnar.timer.views.viewholders.WeekOverviewViewHolder;

import static nl.tcilegnar.timer.fragments.WeekOverviewFragment.Args.DATE_FROM_WEEK;

public class WeekOverviewFragment extends Fragment {
    private final String TAG = Log.getTag(this);
    private static final String DATE_PICKER_DIALOG_TAG = "DATE_PICKER_DIALOG_TAG";

    private TextView weekNumberValueView;
    private TextView totalValueLabelView;
    private TextView totalValueView;
    private LinearLayout weekOverviewListHeader;
    private ListView weekOverviewList;

    private OnDayClickListener dayClickListener;

    public enum Args {
        DATE_FROM_WEEK
    }

    public static WeekOverviewFragment newInstance(@NonNull Calendar dateFromWeek) {
        WeekOverviewFragment fragment = new WeekOverviewFragment();
        Bundle args = new Bundle();
        args.putLong(DATE_FROM_WEEK.name(), dateFromWeek.getTimeInMillis());
        fragment.setArguments(args);
        return fragment;
    }

    private Calendar getDateFromWeek() {
        long millis = getArguments().getLong(DATE_FROM_WEEK.name());
        return TimerCalendar.getCalendarInMillis(millis);
    }

    private void setDateFromWeek(Calendar dateFromWeek) {
        getArguments().putLong(DATE_FROM_WEEK.name(), dateFromWeek.getTimeInMillis());
    }

    public void setDayClickListener(OnDayClickListener dayClickListener) {
        this.dayClickListener = dayClickListener;
    }

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
        weekNumberValueView = view.findViewById(R.id.week_number_value);
        totalValueLabelView = view.findViewById(R.id.total_value_label);
        totalValueView = view.findViewById(R.id.total_value);
        weekOverviewListHeader = view.findViewById(R.id.week_overview_list_header);
        weekOverviewList = view.findViewById(R.id.week_overview_list);

        updateWeekValues(getDateFromWeek());
        setVersionNumber(view);
    }

    private void updateWeekValues(Calendar dateFromWeek) {
        try {
            List<CurrentDayMillis> currentDayMillisOfWeek = getCurrentDayMillisOfWeek(dateFromWeek);
            Week week = new Week(dateFromWeek, currentDayMillisOfWeek);
            setWeekNumber(week);
            setTotalTime(week);
            setYearListHeader();
            updateWeekOverviewList(week);
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
            Log.v(TAG, currentDayMillis.getDayMillis() + " - " + currentDayMillis.toString());
        }
        return currentDayMillisOfWeek;
    }

    private void setWeekNumber(Week week) {
        String weekNumberText = String.valueOf(week.getWeekNumber());
        weekNumberValueView.setText(weekNumberText);
    }

    private void setTotalTime(Week week) {
        int totalTimeInMinutes = week.getTotalTimeInMinutes();
        String timeString = TimerCalendarUtil.getReadableTimeStringHoursAndMinutes(totalTimeInMinutes);
        if (!timeString.isEmpty()) {
            totalValueView.setText(timeString);
            totalValueView.setVisibility(View.VISIBLE);
            totalValueLabelView.setVisibility(View.VISIBLE);
        } else {
            totalValueView.setVisibility(View.GONE);
            totalValueLabelView.setVisibility(View.GONE);
        }
    }

    private void setYearListHeader() {
        ((TextView) weekOverviewListHeader.findViewById(R.id.day_of_week)).setText(Res.getString(R.string
                .list_header_day));
        ((TextView) weekOverviewListHeader.findViewById(R.id.total_time)).setText(Res.getString(R.string
                .list_header_total_time));
    }

    private void updateWeekOverviewList(Week week) {
        WeekOverviewAdapter weekOverviewAdapter = new WeekOverviewAdapter(getActivity(), week);
        weekOverviewList.setAdapter(weekOverviewAdapter);
        weekOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calendar dateOfDay = ((WeekOverviewViewHolder) view).getItem().getDayMillis().getDay();
                dayClickListener.onDayClicked(dateOfDay);
            }
        });
    }

    public void setVersionNumber(View view) {
        TextView versionNrView = view.findViewById(R.id.version_nr);
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
        datePickerFragment.show(getActivity().getFragmentManager(), DATE_PICKER_DIALOG_TAG, getDateFromWeek());
    }

    public void setNewDate(Calendar dateFromWeek) {
        setDateFromWeek(dateFromWeek);
        updateWeekValues(dateFromWeek);
    }

    public interface OnDayClickListener {
        void onDayClicked(Calendar dateOfDay);
    }
}
