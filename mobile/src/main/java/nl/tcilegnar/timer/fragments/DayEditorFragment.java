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

import static nl.tcilegnar.timer.views.DayEditorItemView.NO_TIME;
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
        setTotalTime();
    }

    private void setTotalTime() {
        int workingDayTimeInMinutes = getWorkingDayTimeInMinutes();
        int breakTimeInMinutes = getBreakTimeInMinutes();

        if (workingDayTimeInMinutes != NO_TIME) {
            int totalTimeInMinutes = getTotalTimeInMinutes(workingDayTimeInMinutes, breakTimeInMinutes);
            String timeString = getReadableTimeStringHoursAndMinutes(totalTimeInMinutes);
            totalValueView.setText(timeString);
        } else {
            totalValueView.setText("");
        }
    }

    private int getWorkingDayTimeInMinutes() {
        try {
            Calendar startTime = DayEditorItem.Start.getCurrentTime();
            Calendar endTime = DayEditorItem.End.getCurrentTime();
            return getDateDiff(startTime, endTime, TimeUnit.MINUTES);
        } catch (DayEditorItem.TimeNotSetException e) {
            return NO_TIME;
        }
    }

    private int getBreakTimeInMinutes() {
        try {
            Calendar breakStartTime = DayEditorItem.BreakStart.getCurrentTime();
            Calendar breakEndTime = DayEditorItem.BreakEnd.getCurrentTime();
            return getDateDiff(breakStartTime, breakEndTime, TimeUnit.MINUTES);
        } catch (DayEditorItem.TimeNotSetException e) {
            return NO_TIME;
        }
    }

    private int getTotalTimeInMinutes(int workingDayTimeInMinutes, int breakTimeInMinutes) {
        if (breakTimeInMinutes != NO_TIME) {
            return workingDayTimeInMinutes - breakTimeInMinutes;
        } else {
            return workingDayTimeInMinutes;
        }
    }

    public static int getDateDiff(Calendar cal1, Calendar cal2, TimeUnit timeUnit) {
        long diffInMillis = cal2.getTimeInMillis() - cal1.getTimeInMillis();
        return (int) timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static String getReadableTimeStringHoursAndMinutes(int timeInMinutes) {
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
