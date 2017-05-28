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

import static android.view.View.OnClickListener;

public class DayEditorItemView extends LinearLayout implements OnClickListener, TimePickerDialog.OnTimeSetListener {
    private static final String TIMER_PICKER_DIALOG_TAG = "TIMER_PICKER_DIALOG_TAG";
    public static final int NO_TIME = -1;

    private final DayEditorItem dayEditorItem;
    private final TimePickerDialogListener timePickerDialogListener;
    private final TimeChangeListener timeChangeListener;

    private TextView label;
    private TextView value;
    private ImageButton valueEditButton;

    public DayEditorItemView(Context activityContext, DayEditorItem dayEditorItem, TimePickerDialogListener
            timePickerDialogListener, TimeChangeListener timeChangeListener) {
        super(activityContext);
        inflate(activityContext, R.layout.day_editor_item_view, this);
        this.dayEditorItem = dayEditorItem;
        this.timePickerDialogListener = timePickerDialogListener;
        this.timeChangeListener = timeChangeListener;

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
        setCurrentTimeText(dayEditorItem);
    }

    private void setCurrentTimeText(DayEditorItem dayEditorItem) {
        int hour = dayEditorItem.getHour();
        int minute = dayEditorItem.getMinute();
        setCurrentTimeText(hour, minute);
    }

    private void setCurrentTimeText(int hour, int minute) {
        if (hour == NO_TIME || minute == NO_TIME) {
            value.setText("..:..");
        } else {
            String currentTimeText = CalendarFormat.get24hTimeString(hour, minute);
            value.setText(currentTimeText);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.day_editor_item_label) {
            updateCurrentTime();
        } else if (id == R.id.day_editor_item_value) {
            updateCurrentTime();
        } else if (id == R.id.day_editor_item_value_edit_button) {
            timePickerDialogListener.showTimePickerDialog(this, TIMER_PICKER_DIALOG_TAG);
        }
    }

    private void updateCurrentTime() {
        Calendar currentTime = TimerCalendar.getCurrentDate();
        updateTime(currentTime);
    }

    private void updateTime(Calendar newTime) {
        dayEditorItem.setCurrentTime(newTime);
        dayEditorItem.activate();
        timeChangeListener.onTimeChanged(dayEditorItem);
        setCurrentTimeText(dayEditorItem);
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        Calendar newTime = TimerCalendar.getCurrentDateWithTime(hour, minute);
        updateTime(newTime);
    }

    public void updateEnabled() {
        if (dayEditorItem.isEnabled()) {
            label.setEnabled(true);
            value.setEnabled(true);
        } else {
            label.setEnabled(false);
            value.setEnabled(false);
        }
    }

    public void reset() {
        dayEditorItem.setCurrentTime(NO_TIME, NO_TIME);
        setCurrentTimeText(NO_TIME, NO_TIME);
    }

    public interface TimePickerDialogListener {
        void showTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener, String tag);
    }

    public interface TimeChangeListener {
        void onTimeChanged(DayEditorItem dayEditorItem);
    }
}
