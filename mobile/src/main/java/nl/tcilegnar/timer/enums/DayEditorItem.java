package nl.tcilegnar.timer.enums;

import android.support.annotation.StringRes;

import java.util.Calendar;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.utils.Res;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.storage.Storage;

import static nl.tcilegnar.timer.utils.storage.Storage.Key;
import static nl.tcilegnar.timer.views.DayEditorItemView.NO_TIME;

public enum DayEditorItem {
    Start(R.string.day_editor_item_start, Key.DayEditorStartActive, Key.DayEditorStartHour, Key.DayEditorStartMinute),
    BreakStart(R.string.day_editor_item_break_start, Key.DayEditorBreakStartActive, Key.DayEditorBreakStartHour, Key
            .DayEditorBreakStartMinute),
    BreakEnd(R.string.day_editor_item_break_end, Key.DayEditorBreakEndActive, Key.DayEditorBreakEndHour, Key
            .DayEditorBreakEndMinute),
    End(R.string.day_editor_item_end, Key.DayEditorEndActive, Key.DayEditorEndHour, Key.DayEditorEndMinute);

    public static final int DEFAULT_HOUR_VALUE = NO_TIME;
    public static final int DEFAULT_MINUTE_VALUE = NO_TIME;

    private final String name;
    private final Key dayEditorActiveKey;
    private final Key dayEditorHourKey;
    private final Key dayEditorMinuteKey;

    private final Storage storage = new Storage();

    private boolean enabled = true;

    DayEditorItem(@StringRes int nameResId, Key dayEditorActiveKey, Key dayEditorHourKey, Key dayEditorMinuteKey) {
        this.name = Res.getString(nameResId);
        this.dayEditorActiveKey = dayEditorActiveKey;
        this.dayEditorHourKey = dayEditorHourKey;
        this.dayEditorMinuteKey = dayEditorMinuteKey;
    }

    @Override
    public String toString() {
        return name;
    }

    public Key getDayEditorActiveKey() {
        return dayEditorActiveKey;
    }

    public Key getDayEditorHourKey() {
        return dayEditorHourKey;
    }

    public Key getDayEditorMinuteKey() {
        return dayEditorMinuteKey;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isActivated() {
        return storage.loadIsActiveDayEditor(getDayEditorActiveKey());
    }

    public void activate(boolean activate) {
        storage.saveIsActiveDayEditor(getDayEditorActiveKey(), activate);
    }

    public Calendar getCurrentTime() {
        return TimerCalendar.getCurrentDateWithTime(getHour(), getMinute());
    }

    public int getHour() {
        return storage.loadDayEditorHour(getDayEditorHourKey());
    }

    public int getMinute() {
        return storage.loadDayEditorMinute(getDayEditorMinuteKey());
    }

    public void setCurrentTime(Calendar currentTime) {
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        setCurrentTime(hour, minute);
    }

    public void setCurrentTime(int hour, int minute) {
        storage.saveDayEditorHour(getDayEditorHourKey(), hour);
        storage.saveDayEditorMinute(getDayEditorMinuteKey(), minute);
    }
}
