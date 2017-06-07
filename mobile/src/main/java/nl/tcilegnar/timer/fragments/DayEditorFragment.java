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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import nl.tcilegnar.timer.App;
import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.DayEditorAdapter;
import nl.tcilegnar.timer.dialogs.TimePickerFragment;
import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.fragments.dialogs.BreakTimeValidationErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.TotalTimeValidationErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.WorkingDayTimeValidationErrorDialog;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.database.DatabaseUtil;
import nl.tcilegnar.timer.views.DayEditorItemView.TimeChangedListener;

import static android.widget.Toast.LENGTH_SHORT;
import static nl.tcilegnar.timer.views.DayEditorItemView.INVALID_TIME;
import static nl.tcilegnar.timer.views.DayEditorItemView.NO_TIME;
import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorFragment extends Fragment implements TimePickerDialogListener, TimeChangedListener {
    private ListView dayEditorList;
    private DayEditorAdapter dayEditorAdapter;
    private TextView totalValueView;

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
        // TODO: validatie waarbij tijden workingDayTime vergeleken worden met breakTime
        // het gaat nu bv. nog fout wanneer de breakTime BUITEN de workingDayTime ligt en groter is, of wanneer een
        // van beide breakTime tijden buiten de workingDayTime tijden liggen

        String timeString = getTimeString(workingDayTimeInMinutes, breakTimeInMinutes);
        totalValueView.setText(timeString);
    }

    private String getTimeString(int workingDayTimeInMinutes, int breakTimeInMinutes) {
        String timeString;
        if (workingDayTimeInMinutes != NO_TIME && workingDayTimeInMinutes != INVALID_TIME && breakTimeInMinutes !=
                INVALID_TIME) {
            int totalTimeInMinutes = getTotalTimeInMinutes(workingDayTimeInMinutes, breakTimeInMinutes);
            if (totalTimeInMinutes < 0) {
                new TotalTimeValidationErrorDialog().show(getActivity());
                timeString = "";
            } else {
                timeString = getReadableTimeStringHoursAndMinutes(totalTimeInMinutes);
            }
        } else {
            timeString = "";
        }
        return timeString;
    }

    private int getWorkingDayTimeInMinutes() {
        try {
            Calendar startTime = DayEditorItem.Start.getCurrentTime();
            Calendar endTime = DayEditorItem.End.getCurrentTime();
            int dateDiff = getDateDiff(startTime, endTime, TimeUnit.MINUTES);
            if (dateDiff < 0) {
                new WorkingDayTimeValidationErrorDialog().show(getActivity());
                return INVALID_TIME;
            }
            return dateDiff;
        } catch (DayEditorItem.TimeNotSetException e) {
            return NO_TIME;
        }
    }

    private int getBreakTimeInMinutes() {
        try {
            Calendar breakStartTime = DayEditorItem.BreakStart.getCurrentTime();
            Calendar breakEndTime = DayEditorItem.BreakEnd.getCurrentTime();
            int dateDiff = getDateDiff(breakStartTime, breakEndTime, TimeUnit.MINUTES);
            if (dateDiff < 0) {
                new BreakTimeValidationErrorDialog().show(getActivity());
                return INVALID_TIME;
            }
            return dateDiff;
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
        dayEditorAdapter = new DayEditorAdapter(getActivity(), this, this);
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
                boolean isSaved = saveCurrentDayValues();

                if (isSaved) {
                    resetCurrentDay();
                }
            }
        });
    }

    private boolean saveCurrentDayValues() {
        boolean isSaved = false;
        try {
            List<Calendar> times = new ArrayList<>();
            times.add(DayEditorItem.Start.getCurrentTime());
            times.add(DayEditorItem.BreakStart.getCurrentTime());
            times.add(DayEditorItem.BreakEnd.getCurrentTime());
            times.add(DayEditorItem.End.getCurrentTime());
            Calendar currentDay = getCurrentDay();
            CurrentDayMillis currentDayMillis = new CurrentDayMillis(currentDay, times);
            Log.d(this.getClass().getSimpleName(), currentDayMillis.toString());

            DatabaseUtil util = new DatabaseUtil(new DatabaseUtil.AsyncResponse() {
                @Override
                public void processFinish(Boolean success) {
                    if (!success) {
                        Toast.makeText(App.getContext(), "Could not save values, invalid input!", LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(App.getContext(), "Saved success!", LENGTH_SHORT).show();
                    }
                    //                        isSaved = success; // TODO: async, dus geen return!
                }
            });
            util.execute(currentDayMillis);

            isSaved = true;
        } catch (DayEditorItem.TimeNotSetException e) {
            e.printStackTrace();
        }
        return isSaved;
    }

    private Calendar getCurrentDay() throws DayEditorItem.TimeNotSetException {
        // TODO: Current day based on startTime. Should be independant!
        Calendar startTime = DayEditorItem.Start.getCurrentTime();

        Calendar currentDay = Calendar.getInstance();
        currentDay.clear();
        currentDay.set(Calendar.YEAR, startTime.get(Calendar.YEAR));
        currentDay.set(Calendar.MONTH, startTime.get(Calendar.MONTH));
        currentDay.set(Calendar.DAY_OF_YEAR, startTime.get(Calendar.DAY_OF_YEAR));
        return currentDay;
    }

    private void resetCurrentDay() {
        dayEditorAdapter.reset();
    }

    @Override
    public void onTimeChanged() {
        setTotalTime();
    }
}
