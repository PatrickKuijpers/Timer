package nl.tcilegnar.timer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.WeekOverviewAdapter;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;

public class WeekOverviewFragment extends Fragment {
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
        setTotalTime();
        initWeekOverviewList(view);
    }

    private void setWeekNumber(Calendar currentDate) {
        int weekNumber = currentDate.get(Calendar.WEEK_OF_YEAR);
        weekNumberValueView.setText(String.valueOf(weekNumber));
    }

    private void setTotalTime() {
        String timeString = getTotalTimeString();
        if (!timeString.isEmpty()) {
            totalValueView.setText(timeString);
            totalValueView.setVisibility(View.VISIBLE);
            totalValueLabelView.setVisibility(View.VISIBLE);
        } else {
            totalValueView.setVisibility(View.GONE);
            totalValueLabelView.setVisibility(View.GONE);
        }
    }

    private String getTotalTimeString() {
        return TimerCalendarUtil.getReadableTimeStringHoursAndMinutes(0);
    }

    private void initWeekOverviewList(View view) {
        ListView weekOverviewList = (ListView) view.findViewById(R.id.week_overview_list);
        WeekOverviewAdapter weekOverviewAdapter = new WeekOverviewAdapter(getActivity());
        weekOverviewList.setAdapter(weekOverviewAdapter);
    }
}
