package nl.tcilegnar.timer.fragments;

import com.orm.SugarRecord;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.DayEditorAdapter;
import nl.tcilegnar.timer.dialogs.TimePickerFragment;
import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.fragments.dialogs.BreakTimeValidationErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.TotalTimeValidationErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.WorkingDayTimeValidationErrorDialog;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.DateFormatter;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.MyLocale;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.database.DatabaseSaveUtil;
import nl.tcilegnar.timer.utils.storage.Storage;
import nl.tcilegnar.timer.views.DayEditorItemView.CurrentDateListener;
import nl.tcilegnar.timer.views.DayEditorItemView.TimeChangedListener;

import static nl.tcilegnar.timer.utils.DateFormatter.DATE_FORMAT_SPACES_1_JAN_2000;
import static nl.tcilegnar.timer.views.DayEditorItemView.INVALID_TIME;
import static nl.tcilegnar.timer.views.DayEditorItemView.NO_TIME;
import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorFragment extends Fragment implements CurrentDateListener, TimePickerDialogListener,
        TimeChangedListener {
    private final String TAG = this.getClass().getSimpleName();

    private ListView dayEditorList;
    private DayEditorAdapter dayEditorAdapter;
    private TextView totalValueView;
    private TextView totalValueLabelView;
    private TextView currenDayValueView;

    private final Storage storage = new Storage();
    // TODO: voorlopig enkel huidige datum gebruiken ipv evt laden uit storage (voor andere dag?)
    private final Calendar currentDate = TimerCalendar.getCurrentDay(); // storage.loadDayEditorCurrentDay()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setCurrentDate(currentDate);
        setTotalTime();
    }

    private void initViews(View view) {
        totalValueView = (TextView) view.findViewById(R.id.total_value);
        totalValueLabelView = (TextView) view.findViewById(R.id.total_value_label);
        currenDayValueView = (TextView) view.findViewById(R.id.current_day_value);

        dayEditorList = (ListView) view.findViewById(R.id.day_editor_list);
        dayEditorAdapter = new DayEditorAdapter(getActivity(), this, this, this);
        dayEditorList.setAdapter(dayEditorAdapter);

        initFab(view);
    }

    private void initFab(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCurrentDayValues();
            }
        });
    }

    private void setCurrentDate(Calendar date) {
        String currentDateString = DateFormatter.format(date, DATE_FORMAT_SPACES_1_JAN_2000);
        currenDayValueView.setText(currentDateString);
        storage.saveDayEditorCurrentDay(date);
    }

    private void setTotalTime() {
        int workingDayTimeInMinutes = getWorkingDayTimeInMinutes();
        int breakTimeInMinutes = getBreakTimeInMinutes();
        // TODO: validatie waarbij tijden workingDayTime vergeleken worden met breakTime
        // het gaat nu bv. nog fout wanneer de breakTime BUITEN de workingDayTime ligt en groter is, of wanneer een
        // van beide breakTime tijden buiten de workingDayTime tijden liggen

        String timeString = getTimeString(workingDayTimeInMinutes, breakTimeInMinutes);
        if (!timeString.isEmpty()) {
            totalValueView.setText(timeString);
            totalValueView.setVisibility(View.VISIBLE);
            totalValueLabelView.setVisibility(View.VISIBLE);
        } else {
            totalValueView.setVisibility(View.GONE);
            totalValueLabelView.setVisibility(View.GONE);
        }
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
        return String.format(MyLocale.getLocaleForTranslationAndSigns(), "%d:%02d", hours, minutes);
    }

    @Override
    public void showTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener, String tag) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(onTimeSetListener);
        timePickerFragment.show(getActivity().getFragmentManager(), tag);
    }

    private void saveCurrentDayValues() {
        try {
            List<Calendar> times = new ArrayList<>();
            times.add(DayEditorItem.Start.getCurrentTime());
            times.add(DayEditorItem.BreakStart.getCurrentTime());
            times.add(DayEditorItem.BreakEnd.getCurrentTime());
            times.add(DayEditorItem.End.getCurrentTime());
            CurrentDayMillis currentDayMillis = new CurrentDayMillis(currentDate, times);
            Log.d(TAG, currentDayMillis.toString());

            DatabaseSaveUtil util = new DatabaseSaveUtil(new DatabaseSaveUtil.AsyncResponse() {
                @Override
                public void processFinish(Long savedId, boolean success) {
                    if (success) {
                        logAll();
                        resetCurrentDay();
                    }
                }
            });
            util.execute(currentDayMillis);
        } catch (DayEditorItem.TimeNotSetException e) {
            e.printStackTrace();
        }
    }

    private void logAll() {
        try {
            List<CurrentDayMillis> currentDayMillisList = SugarRecord.listAll(CurrentDayMillis.class);
            Log.i(TAG, "listAll: " + currentDayMillisList.size());
            for (CurrentDayMillis currentDayMillis : currentDayMillisList) {
                Log.i(TAG, currentDayMillis.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetCurrentDay() {
        dayEditorAdapter.reset();
    }

    @Override
    public void onTimeChanged() {
        setTotalTime();
    }

    @Override
    public Calendar getCurrentDate() {
        return currentDate;
    }
}
