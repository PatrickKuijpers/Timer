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
import nl.tcilegnar.timer.adapters.YearOverviewAdapter;
import nl.tcilegnar.timer.dialogs.DatePickerFragment;
import nl.tcilegnar.timer.fragments.dialogs.LoadErrorDialog;
import nl.tcilegnar.timer.models.Year;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.Res;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;
import nl.tcilegnar.timer.views.viewholders.YearOverviewViewHolder;

public class YearOverviewFragment extends Fragment {
    private final String TAG = Log.getTag(this);
    private static final String YEAR_PICKER_DIALOG_TAG = "YEAR_PICKER_DIALOG_TAG";

    private TextView yearValueView;
    private TextView totalValueLabelView;
    private TextView totalValueView;
    private LinearLayout yearOverviewListHeader;
    private ListView yearOverviewList;

    private OnWeekClickListener weekClickListener;

    private enum Args {
        DATE_FROM_YEAR
    }

    public static YearOverviewFragment newInstance(@NonNull Calendar dateFromYear) {
        YearOverviewFragment fragment = new YearOverviewFragment();
        Bundle args = new Bundle();
        args.putLong(Args.DATE_FROM_YEAR.name(), dateFromYear.getTimeInMillis());
        fragment.setArguments(args);
        return fragment;
    }

    private Calendar getDateFromYear() {
        long millis = getArguments().getLong(Args.DATE_FROM_YEAR.name());
        return TimerCalendar.getCalendarInMillis(millis);
    }

    private void setDateFromYear(Calendar dateFromWeek) {
        getArguments().putLong(Args.DATE_FROM_YEAR.name(), dateFromWeek.getTimeInMillis());
    }

    public void setWeekClickListener(OnWeekClickListener weekClickListener) {
        this.weekClickListener = weekClickListener;
    }

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
        yearOverviewListHeader = (LinearLayout) view.findViewById(R.id.year_overview_list_header);
        yearOverviewList = (ListView) view.findViewById(R.id.year_overview_list);

        updateYearValues(getDateFromYear());
    }

    private void updateYearValues(Calendar dateFromYear) {
        try {
            List<CurrentDayMillis> currentDayMillisOfYear = getCurrentDayMillisOfYear(dateFromYear);
            Year year = new Year(dateFromYear, currentDayMillisOfYear);
            setYearNumber(year);
            setTotalTime(year);
            setYearListHeader();
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

    private void setTotalTime(Year year) {
        int totalTimeInMinutes = year.getTotalTimeInMinutes();
        String timeString = TimerCalendarUtil.getReadableTimeStringHoursAndMinutesLetters(totalTimeInMinutes);
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
        ((TextView) yearOverviewListHeader.findViewById(R.id.week_of_year)).setText(Res.getString(R.string
                .list_header_week));
        ((TextView) yearOverviewListHeader.findViewById(R.id.total_time)).setText(Res.getString(R.string
                .list_header_total_time));
        ((TextView) yearOverviewListHeader.findViewById(R.id.first_and_last_day_of_week)).setText(Res.getString(R
                .string.list_header_total_start_week));
    }

    private void updateYearOverviewList(Year year) {
        YearOverviewAdapter yearOverviewAdapter = new YearOverviewAdapter(getActivity(), year);
        yearOverviewList.setAdapter(yearOverviewAdapter);
        yearOverviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calendar firstDayOfWeek = ((YearOverviewViewHolder) view).getItem().getFirstDayOfWeek();
                weekClickListener.onWeekClicked(firstDayOfWeek);
            }
        });
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
        datePickerFragment.show(getActivity().getFragmentManager(), YEAR_PICKER_DIALOG_TAG, getDateFromYear());
    }

    public void setNewDate(Calendar dateFromYear) {
        setDateFromYear(dateFromYear);
        updateYearValues(dateFromYear);
    }

    public interface OnWeekClickListener {
        void onWeekClicked(Calendar someDateFromWeek);
    }
}
