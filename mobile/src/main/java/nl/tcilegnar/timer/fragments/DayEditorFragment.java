package nl.tcilegnar.timer.fragments;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.App;
import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.activities.DayEditorActivity;
import nl.tcilegnar.timer.adapters.DayEditorAdapter;
import nl.tcilegnar.timer.dialogs.DatePickerFragment;
import nl.tcilegnar.timer.dialogs.TimePickerFragment;
import nl.tcilegnar.timer.fragments.dialogs.SaveErrorDialog;
import nl.tcilegnar.timer.fragments.dialogs.ValidationErrorDialogFragment;
import nl.tcilegnar.timer.interfaces.IDayEditorItem;
import nl.tcilegnar.timer.interfaces.IDayEditorItem.TimeNotSetException;
import nl.tcilegnar.timer.models.DayEditorItem;
import nl.tcilegnar.timer.models.TimerError;
import nl.tcilegnar.timer.models.TodayEditorItem;
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

    private TextView currenDayValueView;
    private TextView totalValueLabelView;
    private TextView totalValueView;
    private FloatingActionButton saveButton;
    private FloatingActionButton clearButton;

    private ListView dayEditorListView;
    private DayEditorAdapter dayEditorAdapter;

    private final Storage storage = new Storage();

    private SaveListener saveLisener;

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
        logAll();
    }

    private void initViews(View view) {
        currenDayValueView = view.findViewById(R.id.current_day_value);
        totalValueLabelView = view.findViewById(R.id.total_value_label);
        totalValueView = view.findViewById(R.id.total_value);

        saveButton = view.findViewById(R.id.day_editor_button_save);
        clearButton = view.findViewById(R.id.day_editor_button_clear);

        dayEditorListView = view.findViewById(R.id.day_editor_list);

        updateCurrentDate(getDayEditorDate());
        setListeners();
    }

    /** TODO: Order is important! Can be improved? */
    private void updateCurrentDate(Calendar newCurrentDate) {
        setCurrentDate(newCurrentDate);
        updateDayEditorList();
        setTotalTime(newCurrentDate);
    }

    private void setCurrentDate(Calendar date) {
        setDayEditorDate(date);
        if (isTodayEditor()) {
            storage.saveTodayEditorCurrentDate(date);
        }
        currenDayValueView.setText(getCurrentDayString(date));
    }

    private String getCurrentDayString(Calendar date) {
        String currentDayString = DateFormatter.format(date, DATE_FORMAT_SPACES_1_JAN_2000);
        if (isTodayEditor()) {
            currentDayString += " (" + Res.getString(R.string.today) + ")";
        }
        return currentDayString;
    }

    private void updateDayEditorList() {
        dayEditorAdapter = new DayEditorAdapter(getActivity(), getDayEditorItems(), this, this, this);
        dayEditorListView.setAdapter(dayEditorAdapter);
    }

    private List<IDayEditorItem> getDayEditorItems() {
        if (isTodayEditor()) {
            return TodayEditorItem.getItemsForAllStates();
        } else {
            return DayEditorItem.getItemsForAllStates();
        }
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
        } catch (TimeNotSetException ignored) {
        }
        return timeString;
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
                dayFinishedAndResetCurrentDay();
            }
        });
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
                            dayFinishedAndResetCurrentDay();
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
        } catch (TimeNotSetException e) {
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

    private CurrentDayMillis getDayEditorDateMillis(Calendar date) throws TimeNotSetException {
        List<IDayEditorItem> dayEditorItems = dayEditorAdapter.getAllItems();
        return new CurrentDayMillis(date, dayEditorItems);
    }

    private void logAll() {
        try {
            List<CurrentDayMillis> currentDayMillisList = SugarRecord.listAll(CurrentDayMillis.class);
            Log.i(TAG, "listAll: " + currentDayMillisList.size());
            StringBuilder sb = new StringBuilder();
            for (CurrentDayMillis currentDayMillis : currentDayMillisList) {
                String str = currentDayMillis.csvString();
                Log.i(TAG, str);
                sb.append(str);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dayFinishedAndResetCurrentDay() {
        if (isTodayEditor()) {
            storage.deleteActiveTodayEditor();
        }
        resetCurrentDay();
    }

    private void resetCurrentDay() {
        dayEditorAdapter.reset();
    }

    private boolean isTodayEditor() {
        return TimerCalendarUtil.isToday(getDayEditorDate());
    }

    @Override
    public void onTimeChanged() {
        setTotalTime(getDayEditorDate());
    }

    public interface SaveListener {
        void onSaveSuccessful(Calendar someDateFromWeek);
    }

    private void writeStorageToFile(String currentDayMillisList) throws Exception {
        //        checkExternalMedia();
        //        writeToSDFile(currentDayMillisList);
        //        readRaw();

        write2(currentDayMillisList);
    }

    /**
     * Method to check whether external media available and writable. This is adapted from
     * http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
     */

    private void checkExternalMedia() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        //        tv.append("\n\nExternal Media: readable=" + mExternalStorageAvailable + "
        //        writable=" + mExternalStorageWriteable);
    }

    /**
     * Method to write ascii text characters to file on SD card. Note that you must add a
     * WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw a
     * FileNotFound Exception because you won't have write permission.
     */

    private void writeToSDFile(String currentDayMillisList) {

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        File root = android.os.Environment.getExternalStorageDirectory();
        //        tv.append("\nExternal file system root: " + root);

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "myData.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println("Hi , How are you");
            pw.println("Hello");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "******* File not found. Did you" + " add a WRITE_EXTERNAL_STORAGE " +
                    "permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        tv.append("\n\nFile written to " + file);
    }

    /**
     * Method to read in a text file placed in the res/raw directory of the application. The method
     * reads in all lines of the file sequentially.
     */
    private void readRaw() {
        //        InputStream is = this.getResources().openRawResource(R.raw.textfile);
        //        InputStreamReader isr = new InputStreamReader(is);
        //        BufferedReader br = new BufferedReader(isr, 8192);    // 2nd arg is buffer size
        //
        //        // More efficient (less readable) implementation of above is the composite
        //        expression
        //    /*BufferedReader br = new BufferedReader(new InputStreamReader(
        //            this.getResources().openRawResource(R.raw.textfile)), 8192);*/
        //
        //        try {
        //            String test;
        //            while (true) {
        //                test = br.readLine();
        //                // readLine() returns null if no more lines in the file
        //                if (test == null) {
        //                    break;
        //                }
        //                tv.append("\n" + "    " + test);
        //            }
        //            isr.close();
        //            is.close();
        //            br.close();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //       // tv.append("\n\nThat is all");
    }

    private void write2(String currentDayMillisList) {
        String testText = "test";
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            DayEditorActivity activity = (DayEditorActivity) getActivity();
            if (checkPermission(activity)) {
                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard.getAbsolutePath() + "/text/");
                dir.mkdir();
                File file = new File(dir, "sample.txt");
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                    os.write(testText.getBytes());
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Wrote file to: " + file.getPath());
            } else {
                requestPermission(activity); // Code for permission
            }
        }
    }

    private boolean checkPermission(DayEditorActivity activity) {
        int result = ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private final static int PERMISSION_REQUEST_CODE = 1;

    private void requestPermission(DayEditorActivity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, "Write External Storage permission allows us to " + "create "
                            + "files. Please " + "allow this permission in App Settings.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
    }
}
