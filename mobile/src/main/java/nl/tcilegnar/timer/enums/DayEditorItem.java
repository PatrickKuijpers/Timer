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
    Start(R.string.day_editor_item_start, Key.DayEditorStartDone, Key.DayEditorStartHour, Key.DayEditorStartMinute),
    BreakStart(R.string.day_editor_item_break_start, Key.DayEditorBreakStartDone, Key.DayEditorBreakStartHour, Key
            .DayEditorBreakStartMinute),
    BreakEnd(R.string.day_editor_item_break_end, Key.DayEditorBreakEndDone, Key.DayEditorBreakEndHour, Key
            .DayEditorBreakEndMinute),
    End(R.string.day_editor_item_end, Key.DayEditorEndDone, Key.DayEditorEndHour, Key.DayEditorEndMinute);

    public static final int DEFAULT_HOUR_VALUE = NO_TIME;
    public static final int DEFAULT_MINUTE_VALUE = NO_TIME;

    private final String name;
    private final Key dayEditorDoneKey;
    private final Key dayEditorHourKey;
    private final Key dayEditorMinuteKey;

    private final Storage storage = new Storage();

    private boolean enabled = true;

    DayEditorItem(@StringRes int nameResId, Key dayEditorDoneKey, Key dayEditorHourKey, Key dayEditorMinuteKey) {
        this.name = Res.getString(nameResId);
        this.dayEditorDoneKey = dayEditorDoneKey;
        this.dayEditorHourKey = dayEditorHourKey;
        this.dayEditorMinuteKey = dayEditorMinuteKey;
    }

    @Override
    public String toString() {
        return name;
    }

    public Key getDayEditorDoneKey() {
        return dayEditorDoneKey;
    }

    public Key getDayEditorHourKey() {
        return dayEditorHourKey;
    }

    public Key getDayEditorMinuteKey() {
        return dayEditorMinuteKey;
    }

    public boolean isDone() {
        return storage.loadIsDayEditorDone(getDayEditorDoneKey());
    }

    public void setIsDone(boolean isDone) {
        storage.saveIsDayEditorDone(getDayEditorDoneKey(), isDone);
    }

    public boolean isActive() {
        DayEditorItem dayEditorItem = storage.loadActiveDayEditor();
        return this.equals(dayEditorItem);
    }

    public void setActive() {
        storage.saveActiveDayEditor(this);
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

    public Calendar getCalendarWithTime(Calendar currentDate) throws TimeNotSetException {
        int hour = getHour();
        int minute = getMinute();
        if (hour == DEFAULT_HOUR_VALUE || minute == DEFAULT_MINUTE_VALUE) {
            throw new TimeNotSetException();
        }
        return TimerCalendar.getCalendarWithTime(currentDate, hour, minute);
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

    public class TimeNotSetException extends Exception {
        public TimeNotSetException() {
            super("Time is not set, so cannot be used to calculate time or create a Calendar instance");
        }
    }
}
