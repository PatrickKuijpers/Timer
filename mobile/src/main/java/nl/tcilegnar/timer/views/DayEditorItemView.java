package nl.tcilegnar.timer.views;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.utils.CalendarFormat;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.storage.Storage;

import static android.view.View.OnClickListener;

public class DayEditorItemView extends LinearLayout implements OnClickListener, TimePickerDialog.OnTimeSetListener {
    private static final String TIMER_PICKER_DIALOG_TAG = "TIMER_PICKER_DIALOG_TAG";

    private final DayEditorItem dayEditorItem;
    private final Storage storage = new Storage();
    private final TimePickerDialogListener timePickerDialogListener;

    private TextView label;
    private TextView value;
    private ImageButton valueEditButton;

    public DayEditorItemView(Context activityContext, DayEditorItem dayEditorItem, TimePickerDialogListener
            timePickerDialogListener) {
        super(activityContext);
        inflate(activityContext, R.layout.day_editor_item_view, this);
        this.dayEditorItem = dayEditorItem;
        this.timePickerDialogListener = timePickerDialogListener;

        initViews();
        initValues(dayEditorItem);
    }

    private void initViews() {
        label = (TextView) findViewById(R.id.day_editor_item_label);
        value = (TextView) findViewById(R.id.day_editor_item_value);
        valueEditButton = (ImageButton) findViewById(R.id.day_editor_item_value_edit_button);

        label.setOnClickListener(this);
        value.setOnClickListener(this);
        valueEditButton.setOnClickListener(this);
    }

    private void initValues(DayEditorItem dayEditorItem) {
        label.setText(dayEditorItem.toString());
        int hour = storage.loadDayEditorHour(dayEditorItem.getDayEditorHourKey());
        int minute = storage.loadDayEditorMinute(dayEditorItem.getDayEditorMinuteKey());
        setCurrentTime(hour, minute);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.day_editor_item_label) {
            setCurrentTime();
        } else if (id == R.id.day_editor_item_value) {
            setCurrentTime();
        } else if (id == R.id.day_editor_item_value_edit_button) {
            timePickerDialogListener.showTimePickerDialog(this, TIMER_PICKER_DIALOG_TAG);
        }
    }

    private void setCurrentTime() {
        Calendar currentTime = TimerCalendar.getCurrentDate();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        setCurrentTime(hour, minute);
    }

    private void setCurrentTime(int hour, int minute) {
        String currentTimeText = CalendarFormat.get24hTimeString(hour, minute);
        value.setText(currentTimeText);

        saveValues(hour, minute);
    }

    private void saveValues(int hour, int minute) {
        storage.saveDayEditorHour(dayEditorItem.getDayEditorHourKey(), hour);
        storage.saveDayEditorMinute(dayEditorItem.getDayEditorMinuteKey(), minute);
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        setCurrentTime(hour, minute);
    }

    public interface TimePickerDialogListener {
        void showTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener, String tag);
    }
}
