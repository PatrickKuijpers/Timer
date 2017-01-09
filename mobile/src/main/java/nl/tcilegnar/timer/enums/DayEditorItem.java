package nl.tcilegnar.timer.enums;

import android.support.annotation.StringRes;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.utils.Res;

import static nl.tcilegnar.timer.utils.storage.Storage.Key;

public enum DayEditorItem {
    Start(R.string.day_editor_item_start, Key.DayEditorStartHour, Key.DayEditorStartMinute),
    Break(R.string.day_editor_item_break, Key.DayEditorBreakHour, Key.DayEditorBreakMinute),
    BreakEnd(R.string.day_editor_item_break_end, Key.DayEditorBreakEndHour, Key.DayEditorBreakEndMinute),
    End(R.string.day_editor_item_end, Key.DayEditorEndHour, Key.DayEditorEndMinute);

    public static final int DEFAULT_HOUR_VALUE = 0;
    public static final int DEFAULT_MINUTE_VALUE = 0;

    private final String name;
    private final Key dayEditorHour;
    private final Key dayEditorMinute;
    private boolean enabled = true;

    DayEditorItem(@StringRes int nameResId, Key dayEditorHour, Key dayEditorMinute) {
        this.name = Res.getString(nameResId);
        this.dayEditorHour = dayEditorHour;
        this.dayEditorMinute = dayEditorMinute;
    }

    @Override
    public String toString() {
        return name;
    }

    public Key getDayEditorHourKey() {
        return dayEditorHour;
    }

    public Key getDayEditorMinuteKey() {
        return dayEditorMinute;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
