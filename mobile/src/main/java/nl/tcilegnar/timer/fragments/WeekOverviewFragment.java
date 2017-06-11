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

public class WeekOverviewFragment extends Fragment {
    private final Calendar currentDate = TimerCalendar.getCurrent();

    private TextView weekNumberValueView;

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

        setWeekNumber(currentDate);
        initWeekOverviewList(view);
    }

    private void setWeekNumber(Calendar currentDate) {
        int weekNumber = currentDate.get(Calendar.WEEK_OF_YEAR);
        weekNumberValueView.setText(String.valueOf(weekNumber));
    }

    private void initWeekOverviewList(View view) {
        ListView weekOverviewList = (ListView) view.findViewById(R.id.week_overview_list);
        WeekOverviewAdapter weekOverviewAdapter = new WeekOverviewAdapter(getActivity());
        weekOverviewList.setAdapter(weekOverviewAdapter);
    }
}
