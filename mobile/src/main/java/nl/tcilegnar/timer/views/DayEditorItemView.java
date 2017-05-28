package nl.tcilegnar.timer.views;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    public static final int INVALID_TIME = -2;

    private final DayEditorItem dayEditorItem;
    private final TimePickerDialogListener timePickerDialogListener;
    private final TimeChangedListener timeChangedListener;
    private final ActiveItemChangeListener activeItemChangeListener;

    private ImageView imageDone;
    private TextView label;
    private TextView timeValue;
    private ImageButton timeValueEditButton;

    public DayEditorItemView(Context activityContext, DayEditorItem dayEditorItem, TimePickerDialogListener
            timePickerDialogListener, TimeChangedListener timeChangedListener, ActiveItemChangeListener
            activeItemChangeListener) {
        super(activityContext);
        inflate(activityContext, R.layout.day_editor_item_view, this);
        this.dayEditorItem = dayEditorItem;
        this.timePickerDialogListener = timePickerDialogListener;
        this.timeChangedListener = timeChangedListener;
        this.activeItemChangeListener = activeItemChangeListener;

        initViews();
        initValues(dayEditorItem);
    }

    private void initViews() {
        imageDone = (ImageView) findViewById(R.id.day_editor_item_done);
        label = (TextView) findViewById(R.id.day_editor_item_label);
        timeValue = (TextView) findViewById(R.id.day_editor_item_value);
        timeValueEditButton = (ImageButton) findViewById(R.id.day_editor_item_value_edit_button);

        imageDone.setOnClickListener(this);
        label.setOnClickListener(this);
        timeValue.setOnClickListener(this);
        timeValueEditButton.setOnClickListener(this);
    }

    private void initValues(DayEditorItem dayEditorItem) {
        label.setText(dayEditorItem.toString());
        setCurrentTimeText(dayEditorItem);
        setItemDoneView(dayEditorItem);
    }

    private void setCurrentTimeText(DayEditorItem dayEditorItem) {
        int hour = dayEditorItem.getHour();
        int minute = dayEditorItem.getMinute();
        setCurrentTimeText(hour, minute);
    }

    private void setCurrentTimeText(int hour, int minute) {
        if (hour == NO_TIME || minute == NO_TIME) {
            timeValue.setText("..:..");
        } else {
            String currentTimeText = CalendarFormat.get24hTimeString(hour, minute);
            timeValue.setText(currentTimeText);
        }
    }

    private void setItemDoneView(DayEditorItem dayEditorItem) {
        setItemDoneView(dayEditorItem.isDone());
    }

    private void setItemDoneView(boolean isDone) {
        if (isDone) {
            imageDone.setVisibility(VISIBLE);
        } else {
            imageDone.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == imageDone || view == label) {
            Calendar currentTime = TimerCalendar.getCurrentDate();
            updateTime(currentTime);
        } else if (view == timeValue || view == timeValueEditButton) {
            timePickerDialogListener.showTimePickerDialog(this, TIMER_PICKER_DIALOG_TAG);
        }
    }

    private void updateTime(Calendar newTime) {
        dayEditorItem.setCurrentTime(newTime);
        setCurrentTimeText(dayEditorItem);
        timeChangedListener.onTimeChanged();

        activateItem();
    }

    private void activateItem() {
        if (!dayEditorItem.isDone()) {
            setItemDone(true);
            dayEditorItem.setActive();
            activeItemChangeListener.onActiveItemChanged(dayEditorItem);
        }
    }

    private void setItemDone(boolean shouldBeDone) {
        setItemDoneView(shouldBeDone);
        dayEditorItem.setIsDone(shouldBeDone);
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        Calendar newTime = TimerCalendar.getCurrentDateWithTime(hour, minute);
        updateTime(newTime);
    }

    public void updateEnabledViews() {
        if (dayEditorItem.isEnabled()) {
            imageDone.setEnabled(true);
            label.setEnabled(true);
        } else {
            imageDone.setEnabled(false);
            label.setEnabled(false);
        }
    }

    public void resetTime() {
        dayEditorItem.setCurrentTime(NO_TIME, NO_TIME);
        setCurrentTimeText(NO_TIME, NO_TIME);
        timeChangedListener.onTimeChanged();
        setItemDone(false);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " with item: " + dayEditorItem.name();
    }

    public interface TimePickerDialogListener {
        void showTimePickerDialog(TimePickerDialog.OnTimeSetListener onTimeSetListener, String tag);
    }

    public interface TimeChangedListener {
        void onTimeChanged();
    }

    public interface ActiveItemChangeListener {
        void onActiveItemChanged(DayEditorItem dayEditorItem);
    }
}
