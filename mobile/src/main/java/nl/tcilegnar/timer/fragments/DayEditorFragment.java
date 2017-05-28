package nl.tcilegnar.timer.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.DayEditorAdapter;
import nl.tcilegnar.timer.dialogs.TimePickerFragment;
import nl.tcilegnar.timer.enums.DayEditorItem;

import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorFragment extends Fragment implements TimePickerDialogListener {
    private ListView dayEditorList;
    private DayEditorAdapter dayEditorAdapter;
    private TextView totalValueView;

    public DayEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        calculateTotalTime();
    }

    private void calculateTotalTime() {
        Calendar startTime = DayEditorItem.Start.getCurrentTime();
        Calendar endTime = DayEditorItem.End.getCurrentTime();
        long workingDayTime = getDateDiff(startTime, endTime, TimeUnit.MINUTES);

        Calendar breakStartTime = DayEditorItem.BreakStart.getCurrentTime();
        Calendar breakEndTime = DayEditorItem.BreakEnd.getCurrentTime();
        long breakTime = getDateDiff(breakStartTime, breakEndTime, TimeUnit.MINUTES);

        long totalTime = workingDayTime - breakTime;
        String timeString = getReadableTimeString((int) totalTime);
        totalValueView.setText(timeString);
    }

    public static long getDateDiff(Calendar cal1, Calendar cal2, TimeUnit timeUnit) {
        long diffInMillis = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public String getReadableTimeString(int timeInMinutes) {
        int hours = timeInMinutes / 60;
        int minutes = timeInMinutes % 60;
        return String.format(Locale.getDefault(), "%d:%02d", hours, minutes);
    }

    private void initViews(View view) {
        totalValueView = (TextView) view.findViewById(R.id.total_value);

        dayEditorList = (ListView) view.findViewById(R.id.day_editor_list);
        dayEditorAdapter = new DayEditorAdapter(getActivity(), this);
        dayEditorList.setAdapter(dayEditorAdapter);

        initFab(view);
    }

    @Override
    public void showTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener, String tag) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(onTimeSetListener);
        timePickerFragment.show(getActivity().getFragmentManager(), tag);
    }

    private void initFab(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCurrentDay();
            }
        });
    }

    private void resetCurrentDay() {
        dayEditorAdapter.reset();
    }
}
