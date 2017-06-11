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

import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.App;
import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.DayEditorAdapter;
import nl.tcilegnar.timer.dialogs.DatePickerFragment;
import nl.tcilegnar.timer.dialogs.TimePickerFragment;
import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.fragments.dialogs.SaveErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.ValidationErrorDialogFragment;
import nl.tcilegnar.timer.models.Validation;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.DateFormatter;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.Res;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;
import nl.tcilegnar.timer.utils.database.DatabaseSaveUtil;
import nl.tcilegnar.timer.utils.storage.Storage;
import nl.tcilegnar.timer.views.DayEditorItemView.CurrentDateListener;
import nl.tcilegnar.timer.views.DayEditorItemView.TimeChangedListener;

import static android.widget.Toast.LENGTH_SHORT;
import static nl.tcilegnar.timer.utils.DateFormatter.DATE_FORMAT_SPACES_1_JAN_2000;
import static nl.tcilegnar.timer.utils.TimerCalendar.getCalendarWithTime;
import static nl.tcilegnar.timer.utils.storage.Storage.NO_ACTIVE_DAY_EDITOR;
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

    private SaveListener saveLisener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setCurrentDate(getCurrentDateToInitWith());
        setTotalTime();
    }

    public Calendar getCurrentDateToInitWith() {
        if (storage.loadActiveDayEditor() == NO_ACTIVE_DAY_EDITOR) {
            // No day editor active = no time set: assume you'd like to start over with a new day instead of retreiving
            return TimerCalendar.getCurrentDate();
        } else {
            return getCurrentDate();
        }
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
        storage.saveDayEditorCurrentDate(date);
        String currentDayString = getCurrentDayString(date);
        currenDayValueView.setText(currentDayString);
    }

    private String getCurrentDayString(Calendar date) {
        String currentDayString = DateFormatter.format(date, DATE_FORMAT_SPACES_1_JAN_2000);
        if (TimerCalendarUtil.isToday(date)) {
            currentDayString += " (" + Res.getString(R.string.today) + ")";
        }
        return currentDayString;
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
                int totalTimeInMinutes = currentDayMillis.getTotalTimeInMinutes();
                timeString = TimerCalendarUtil.getReadableTimeStringHoursAndMinutes(totalTimeInMinutes);
            } else {
                new ValidationErrorDialogFragment().show(getActivity());
            }
        } catch (DayEditorItem.TimeNotSetException ignored) {
        }
        return timeString;
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
            return TimerCalendar.getCalendarWithCurrentTime(getCurrentDate());
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
        datePickerFragment.show(getActivity().getFragmentManager(), DATE_PICKER_DIALOG_TAG, getCurrentDate());
    }

    public void setSaveListener(SaveListener saveListener) {
        this.saveLisener = saveListener;
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
                            saveLisener.onSaveSuccessful();
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
        return new CurrentDayMillis(getCurrentDate());
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
        return storage.loadDayEditorCurrentDate();
    }

    public interface SaveListener {
        void onSaveSuccessful();
    }
}
