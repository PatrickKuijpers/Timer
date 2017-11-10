package nl.tcilegnar.timer.fragments;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import nl.tcilegnar.timer.App;
import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.adapters.DayEditorAdapter;
import nl.tcilegnar.timer.dialogs.DatePickerFragment;
import nl.tcilegnar.timer.dialogs.TimePickerFragment;
import nl.tcilegnar.timer.fragments.dialogs.SaveErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.ValidationErrorDialogFragment;
import nl.tcilegnar.timer.interfaces.IDayEditorItem;
import nl.tcilegnar.timer.models.DayEditorItem;
import nl.tcilegnar.timer.models.TimerError;
import nl.tcilegnar.timer.models.Validation;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.DateFormatter;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.Res;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;
import nl.tcilegnar.timer.utils.database.DatabaseSaveUtil;
import nl.tcilegnar.timer.utils.database.DatabaseSaveUtil.AsyncResponse;
import nl.tcilegnar.timer.utils.storage.Storage;
import nl.tcilegnar.timer.views.DayEditorItemView.DayEditorListener;
import nl.tcilegnar.timer.views.DayEditorItemView.TimeChangedListener;

import static android.widget.Toast.LENGTH_SHORT;
import static nl.tcilegnar.timer.fragments.DayEditorFragment.Args.DAY_DATE;
import static nl.tcilegnar.timer.utils.DateFormatter.DATE_FORMAT_SPACES_1_JAN_2000;
import static nl.tcilegnar.timer.utils.TimerCalendar.getCalendarWithTime;
import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorFragment extends Fragment implements DayEditorListener, TimePickerDialogListener,
        TimeChangedListener {
    private final String TAG = Log.getTag(this);
    private static final String DATE_PICKER_DIALOG_TAG = "DATE_PICKER_DIALOG_TAG";

    private DayEditorAdapter dayEditorAdapter;
    private TextView currenDayValueView;
    private TextView totalValueLabelView;
    private TextView totalValueView;
    private FloatingActionButton saveButton;
    private FloatingActionButton clearButton;

    private final Storage storage = new Storage();

    private SaveListener saveLisener;

    private List<IDayEditorItem> dayEditorItems;

    public enum Args {
        DAY_DATE
    }

    public static DayEditorFragment newInstance(@NonNull Calendar dayDate) {
        DayEditorFragment fragment = new DayEditorFragment();
        Bundle args = new Bundle();
        args.putLong(DAY_DATE.name(), dayDate.getTimeInMillis());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Calendar getDayEditorDate() {
        long millis = getArguments().getLong(DAY_DATE.name());
        return TimerCalendar.getCalendarInMillis(millis);
    }

    private void setDayEditorDate(Calendar date) {
        getArguments().putLong(DAY_DATE.name(), date.getTimeInMillis());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        currenDayValueView = view.findViewById(R.id.current_day_value);
        totalValueLabelView = view.findViewById(R.id.total_value_label);
        totalValueView = view.findViewById(R.id.total_value);

        saveButton = view.findViewById(R.id.day_editor_button_save);
        clearButton = view.findViewById(R.id.day_editor_button_clear);

        Calendar dayEditorDate = getDayEditorDate();
        setCurrentDate(dayEditorDate);
        initDayEditorList(view, dayEditorDate);
        setTotalTime(dayEditorDate);

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
        storage.saveTodayEditorCurrentDate(date); // TODO: only for today
        currenDayValueView.setText(getCurrentDayString(date));
    }

    private String getCurrentDayString(Calendar date) {
        String currentDayString = DateFormatter.format(date, DATE_FORMAT_SPACES_1_JAN_2000);
        if (TimerCalendarUtil.isToday(date)) {
            currentDayString += " (" + Res.getString(R.string.today) + ")";
        }
        return currentDayString;
    }

    private void setTotalTime(Calendar date) {
        String timeString = getTotalTimeString(date);
        if (!timeString.isEmpty()) {
            totalValueView.setText(timeString);
            totalValueView.setVisibility(View.VISIBLE);
            totalValueLabelView.setVisibility(View.VISIBLE);
        } else {
            totalValueView.setVisibility(View.GONE);
            totalValueLabelView.setVisibility(View.GONE);
        }
    }

    private String getTotalTimeString(Calendar date) {
        String timeString = "";
        try {
            CurrentDayMillis currentDayMillis = getDayEditorDateMillis(date);
            Log.d(TAG, currentDayMillis.toString());

            Validation validation = currentDayMillis.getValidation();
            if (validation.isValid()) {
                timeString = currentDayMillis.getTotalTimeReadableString();
            } else {
                new ValidationErrorDialogFragment().show(getActivity());
            }
        } catch (DayEditorItem.TimeNotSetException ignored) {
        }
        return timeString;
    }

    private void initDayEditorList(View view, Calendar dayEditorDate) {
        ListView dayEditorListView = view.findViewById(R.id.day_editor_list);
        dayEditorItems = getDayEditorItems(dayEditorDate);
        dayEditorAdapter = new DayEditorAdapter(getActivity(), dayEditorItems, this, this, this);
        dayEditorListView.setAdapter(dayEditorAdapter);
    }

    private List<IDayEditorItem> getDayEditorItems(Calendar dayEditorDate) {
        if (TimerCalendarUtil.isToday(dayEditorDate)) {
            return DayEditorItem.getItemsForAllStates();
            //            return TodayEditorItem.getItemsForAllStates();
        } else {
            return new ArrayList<>();
            //            return DayEditorItem.getItemsForAllStates();
        }
    }

    @Override
    public void showTimePickerDialog(OnTimeSetListener onTimeSetListener, String tag, IDayEditorItem dayEditorItem) {
        Calendar timeInDatePicker = getTimeToShowInTimePickerDialog(dayEditorItem);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setOnTimeSetListener(onTimeSetListener);
        timePickerFragment.show(getActivity().getFragmentManager(), tag, timeInDatePicker);
    }

    private Calendar getTimeToShowInTimePickerDialog(IDayEditorItem dayEditorItem) {
        if (dayEditorItem.isDone()) {
            int hour = dayEditorItem.getHour();
            int minute = dayEditorItem.getMinute();
            return getCalendarWithTime(getDayEditorDate(), hour, minute);
        } else {
            return TimerCalendar.getCalendarWithCurrentTime(getDayEditorDate());
        }
    }

    public void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newCurrentDate = TimerCalendar.getCalendarWithDate(year, month, dayOfMonth);
                updateCurrentDate(newCurrentDate);
            }
        });
        datePickerFragment.show(getActivity().getFragmentManager(), DATE_PICKER_DIALOG_TAG, getDayEditorDate());
    }

    private void updateCurrentDate(Calendar newCurrentDate) {
        setDayEditorDate(newCurrentDate);
        setCurrentDate(newCurrentDate);
        updateDayEditorList(newCurrentDate);
        setTotalTime(newCurrentDate);
    }

    private void updateDayEditorList(Calendar newCurrentDate) {
        dayEditorItems.clear();
        dayEditorItems.addAll(getDayEditorItems(newCurrentDate));
        dayEditorAdapter.notifyDataSetChanged();
    }

    public void setSaveListener(SaveListener saveListener) {
        this.saveLisener = saveListener;
    }

    private void saveCurrentDayValues() {
        try {
            final CurrentDayMillis currentDayMillis = getDayEditorDateMillis(getDayEditorDate());
            Log.d(TAG, currentDayMillis.toString());

            Validation validation = currentDayMillis.getValidation();
            if (validation.isValid()) {
                final List<CurrentDayMillis> duplicateEntries = getDuplicateEntries(currentDayMillis);
                new DatabaseSaveUtil(new AsyncResponse() {
                    @Override
                    public void savedSuccesfully(Long savedId) {
                        Toast.makeText(App.getContext(), "Saved success (id=" + savedId + ")", LENGTH_SHORT).show();
                        logAll();
                        try {
                            removeDuplicateEntries(duplicateEntries);
                            logAll();
                            resetCurrentDay();
                            saveLisener.onSaveSuccessful(currentDayMillis.getDay());
                        } catch (Exception e) {
                            new SaveErrorDialog(Res.getString(R.string
                                    .error_message_dialog_save_duplciates_could_not_be_removed)).show(getActivity());
                        }
                    }

                    private void removeDuplicateEntries(List<CurrentDayMillis> duplicateEntries) {
                        for (CurrentDayMillis duplicateEntry : duplicateEntries) {
                            duplicateEntry.delete();
                        }
                    }

                    @Override
                    public void saveFailed(TimerError error) {
                        new SaveErrorDialog(error.getMessage()).show(getActivity());
                    }
                }).execute(currentDayMillis);
            } else {
                new SaveErrorDialog(validation.getErrorMessage()).show(getActivity());
            }
        } catch (DayEditorItem.TimeNotSetException e) {
            e.printStackTrace();
            new SaveErrorDialog(Res.getString(R.string.validation_error_message_not_all_times_set)).show(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
            new SaveErrorDialog(e.getMessage()).show(getActivity());
        }
    }

    private List<CurrentDayMillis> getDuplicateEntries(CurrentDayMillis newEntry) {
        long dayMillis = newEntry.getDayMillis();
        Condition sameDay = Condition.prop("DAY_IN_MILLIS").eq(dayMillis);

        List<CurrentDayMillis> duplicateEntries = Select.from(CurrentDayMillis.class).where(sameDay).list();
        Log.i(TAG, duplicateEntries.size() + " duplicate entries found");
        for (CurrentDayMillis duplicateEntry : duplicateEntries) {
            Log.v(TAG, duplicateEntry.toString());
        }
        return duplicateEntries;
    }

    private CurrentDayMillis getDayEditorDateMillis(Calendar date) throws IDayEditorItem.TimeNotSetException {
        return new CurrentDayMillis(date);
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
        setTotalTime(getDayEditorDate());
    }

    public interface SaveListener {
        void onSaveSuccessful(Calendar someDateFromWeek);
    }
}
