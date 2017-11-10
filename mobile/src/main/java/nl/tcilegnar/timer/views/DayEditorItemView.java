package nl.tcilegnar.timer.views;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.interfaces.IDayEditorItem;
import nl.tcilegnar.timer.utils.CalendarFormat;
import nl.tcilegnar.timer.utils.TimerCalendar;

import static android.view.View.OnClickListener;

public class DayEditorItemView extends LinearLayout implements OnClickListener, OnTimeSetListener {
    private static final String TIMER_PICKER_DIALOG_TAG = "TIMER_PICKER_DIALOG_TAG";
    public static final int NO_TIME = -1;

    private final IDayEditorItem dayEditorItem;
    private final DayEditorListener dayEditorListener;
    private final TimePickerDialogListener timePickerDialogListener;
    private final TimeChangedListener timeChangedListener;
    private final ActiveItemChangeListener activeItemChangeListener;

    private ImageView imageDone;
    private TextView label;
    private TextView timeValue;
    private ImageButton timeValueEditButton;

    public DayEditorItemView(Context activityContext, IDayEditorItem dayEditorItem, DayEditorListener
            dayEditorListener, TimePickerDialogListener timePickerDialogListener, TimeChangedListener
            timeChangedListener, ActiveItemChangeListener activeItemChangeListener) {
        super(activityContext);
        inflate(activityContext, R.layout.day_editor_item_view, this);
        this.dayEditorItem = dayEditorItem;
        this.dayEditorListener = dayEditorListener;
        this.timePickerDialogListener = timePickerDialogListener;
        this.timeChangedListener = timeChangedListener;
        this.activeItemChangeListener = activeItemChangeListener;

        initViews();
        initValues(dayEditorItem);
    }

    private void initViews() {
        imageDone = findViewById(R.id.day_editor_item_done);
        label = findViewById(R.id.day_editor_item_label);
        timeValue = findViewById(R.id.day_editor_item_value);
        timeValueEditButton = findViewById(R.id.day_editor_item_value_edit_button);

        imageDone.setOnClickListener(this);
        label.setOnClickListener(this);
        timeValue.setOnClickListener(this);
        timeValueEditButton.setOnClickListener(this);
    }

    private void initValues(IDayEditorItem dayEditorItem) {
        label.setText(dayEditorItem.getState().toString());
        setCurrentTimeText(dayEditorItem);
        setItemDoneView(dayEditorItem);
    }

    private void setCurrentTimeText(IDayEditorItem dayEditorItem) {
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

    private void setItemDoneView(IDayEditorItem dayEditorItem) {
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
            updateTime(TimerCalendar.getCalendarWithCurrentTime(getCurrentDate()));
        } else if (view == timeValue || view == timeValueEditButton) {
            timePickerDialogListener.showTimePickerDialog(this, TIMER_PICKER_DIALOG_TAG, dayEditorItem);
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
        Calendar newTime = TimerCalendar.getCalendarWithTime(getCurrentDate(), hour, minute);
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

    public Calendar getCurrentDate() {
        return dayEditorListener.getDayEditorDate();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " with item: " + dayEditorItem.getState().name();
    }

    public interface TimePickerDialogListener {
        void showTimePickerDialog(OnTimeSetListener onTimeSetListener, String tag, IDayEditorItem dayEditorItem);
    }

    public interface DayEditorListener {
        Calendar getDayEditorDate();
    }

    public interface TimeChangedListener {
        void onTimeChanged();
    }

    public interface ActiveItemChangeListener {
        void onActiveItemChanged(IDayEditorItem dayEditorItem);
    }
}
