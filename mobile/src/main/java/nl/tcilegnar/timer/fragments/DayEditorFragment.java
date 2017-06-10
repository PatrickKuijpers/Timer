package nl.tcilegnar.timer.fragments;

import com.orm.SugarRecord;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.tcilegnar.timer.App;
import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.DayEditorAdapter;
import nl.tcilegnar.timer.dialogs.DatePickerFragment;
import nl.tcilegnar.timer.dialogs.TimePickerFragment;
import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.fragments.dialogs.BreakTimeValidationErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.SaveErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.TotalTimeValidationErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.WorkingDayTimeValidationErrorDialog;
import nl.tcilegnar.timer.models.Validation;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.DateFormatter;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.MyLocale;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.database.DatabaseSaveUtil;
import nl.tcilegnar.timer.utils.storage.Storage;
import nl.tcilegnar.timer.views.DayEditorItemView.CurrentDateListener;
import nl.tcilegnar.timer.views.DayEditorItemView.TimeChangedListener;

import static android.widget.Toast.LENGTH_SHORT;
import static nl.tcilegnar.timer.enums.DayEditorItem.BreakEnd;
import static nl.tcilegnar.timer.enums.DayEditorItem.BreakStart;
import static nl.tcilegnar.timer.enums.DayEditorItem.DEFAULT_HOUR_VALUE;
import static nl.tcilegnar.timer.enums.DayEditorItem.DEFAULT_MINUTE_VALUE;
import static nl.tcilegnar.timer.enums.DayEditorItem.End;
import static nl.tcilegnar.timer.enums.DayEditorItem.Start;
import static nl.tcilegnar.timer.utils.DateFormatter.DATE_FORMAT_SPACES_1_JAN_2000;
import static nl.tcilegnar.timer.utils.TimerCalendar.getCalendarWithTime;
import static nl.tcilegnar.timer.views.DayEditorItemView.INVALID_TIME;
import static nl.tcilegnar.timer.views.DayEditorItemView.NO_TIME;
import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorFragment extends Fragment implements CurrentDateListener, TimePickerDialogListener,
        TimeChangedListener {
    private final String TAG = this.getClass().getSimpleName();
    private static final String DATE_PICKER_DIALOG_TAG = "DATE_PICKER_DIALOG_TAG";

    private DayEditorAdapter dayEditorAdapter;
    private TextView totalValueView;
    private TextView totalValueLabelView;
    private TextView currenDayValueView;
    private FloatingActionButton saveButton;
    private FloatingActionButton clearButton;

    private final Storage storage = new Storage();
    // TODO: voorlopig enkel huidige datum gebruiken ipv evt laden uit storage (voor andere dag?)
    private Calendar currentDate = TimerCalendar.getCurrentDate(); // storage.loadDayEditorCurrentDay()

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

        saveButton = (FloatingActionButton) view.findViewById(R.id.day_editor_button_save);
        clearButton = (FloatingActionButton) view.findViewById(R.id.day_editor_button_clear);

        ListView dayEditorList = (ListView) view.findViewById(R.id.day_editor_list);
        dayEditorAdapter = new DayEditorAdapter(getActivity(), this, this, this);
        dayEditorList.setAdapter(dayEditorAdapter);

        setListeners();
    }

    private void setListeners() {
        currenDayValueView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCurrentDayValues();
            }
        });
        clearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCurrentDay();
            }
        });
    }

    private void setCurrentDate(Calendar date) {
        currentDate = date;
        String currentDateString = DateFormatter.format(date, DATE_FORMAT_SPACES_1_JAN_2000);
        currenDayValueView.setText(currentDateString);
        storage.saveDayEditorCurrentDay(date);
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
        String timeString = "";
        try {
            CurrentDayMillis currentDayMillis = getCurrentDayMillis();
            Log.d(TAG, currentDayMillis.toString());

            Validation validation = currentDayMillis.getValidation();
            if (validation.isValid()) {
                // TODO: kan nu vast makkelijker!
                int workingDayTimeInMinutes = getWorkingDayTimeInMinutes();
                int breakTimeInMinutes = getBreakTimeInMinutes();

                timeString = getTotalTimeString(workingDayTimeInMinutes, breakTimeInMinutes);
            } else {
                new TotalTimeValidationErrorDialog().show(getActivity());
            }
        } catch (DayEditorItem.TimeNotSetException ignored) {
        }
        return timeString;
    }

    private String getTotalTimeString(int workingDayTimeInMinutes, int breakTimeInMinutes) {
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
        if (Start.getHour() != DEFAULT_HOUR_VALUE && End.getHour() != DEFAULT_HOUR_VALUE && Start.getMinute() !=
                DEFAULT_MINUTE_VALUE && End.getMinute() != DEFAULT_MINUTE_VALUE) {
            Calendar startOfDay = getCalendarWithTime(currentDate, Start.getHour(), Start.getMinute());
            Calendar endOfDay = getCalendarWithTime(currentDate, End.getHour(), End.getMinute());
            int dateDiff = getDateDiff(startOfDay, endOfDay, TimeUnit.MINUTES);
            if (dateDiff < 0) {
                new WorkingDayTimeValidationErrorDialog().show(getActivity());
                return INVALID_TIME;
            }
            return dateDiff;
        } else {
            return NO_TIME;
        }
    }

    private int getBreakTimeInMinutes() {
        if (BreakStart.getHour() != DEFAULT_HOUR_VALUE && BreakEnd.getHour() != DEFAULT_HOUR_VALUE && BreakStart
                .getMinute() != DEFAULT_MINUTE_VALUE && BreakEnd.getMinute() != DEFAULT_MINUTE_VALUE) {
            Calendar breakStartTime = getCalendarWithTime(currentDate, BreakStart.getHour(), BreakStart.getMinute());
            Calendar breakEndTime = getCalendarWithTime(currentDate, BreakEnd.getHour(), BreakEnd.getMinute());
            int dateDiff = getDateDiff(breakStartTime, breakEndTime, TimeUnit.MINUTES);
            if (dateDiff < 0) {
                new BreakTimeValidationErrorDialog().show(getActivity());
                return INVALID_TIME;
            }
            return dateDiff;
        } else {
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
    public void showTimePickerDialog(OnTimeSetListener onTimeSetListener, String tag, DayEditorItem dayEditorItem) {
        Calendar timeInDatePicker = getTimeToShowInTimePickerDialog(dayEditorItem);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(onTimeSetListener);
        timePickerFragment.show(getActivity().getFragmentManager(), tag, timeInDatePicker);
    }

    private Calendar getTimeToShowInTimePickerDialog(DayEditorItem dayEditorItem) {
        if (dayEditorItem.isDone()) {
            int hour = dayEditorItem.getHour();
            int minute = dayEditorItem.getMinute();
            return getCalendarWithTime(getCurrentDate(), hour, minute);
        } else {
            return TimerCalendar.getCalendarWithCurrentTime(currentDate);
        }
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newCurrentDate = TimerCalendar.getCalendarWithDate(year, month, dayOfMonth);
                setCurrentDate(newCurrentDate);
            }
        });
        datePickerFragment.show(getActivity().getFragmentManager(), DATE_PICKER_DIALOG_TAG, currentDate);
    }

    private void saveCurrentDayValues() {
        try {
            CurrentDayMillis currentDayMillis = getCurrentDayMillis();
            Log.d(TAG, currentDayMillis.toString());

            Validation validation = currentDayMillis.getValidation();
            if (validation.isValid()) {
                DatabaseSaveUtil util = new DatabaseSaveUtil(new DatabaseSaveUtil.AsyncResponse() {
                    @Override
                    public void processFinish(Long savedId, boolean success) {
                        if (success) {
                            Toast.makeText(App.getContext(), "Saved success (id=" + savedId + ")", LENGTH_SHORT).show();
                            logAll();
                            resetCurrentDay();
                        } else {
                            new SaveErrorDialog("Save failed").show(getActivity());
                        }
                    }
                });
                util.execute(currentDayMillis);
            } else {
                new SaveErrorDialog(validation).show(getActivity());
            }
        } catch (DayEditorItem.TimeNotSetException e) {
            new SaveErrorDialog(e.getMessage()).show(getActivity());
        }
    }

    private CurrentDayMillis getCurrentDayMillis() throws DayEditorItem.TimeNotSetException {
        List<Calendar> times = new ArrayList<>();
        times.add(Start.getCalendarWithTime(currentDate));
        times.add(BreakStart.getCalendarWithTime(currentDate));
        times.add(BreakEnd.getCalendarWithTime(currentDate));
        times.add(End.getCalendarWithTime(currentDate));
        return new CurrentDayMillis(currentDate, times);
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
