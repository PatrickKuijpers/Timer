package nl.tcilegnar.timer.enums;

import android.support.annotation.StringRes;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.utils.Res;

import static nl.tcilegnar.timer.utils.storage.Storage.Key;

/** Order of enums should not be changed (due to calculations in for-loops etc)! */
public enum DayEditorItemState {
    Start(R.string.day_editor_item_start, Key.DayEditorStartDone, Key.DayEditorStartHour, Key.DayEditorStartMinute),
    BreakStart(R.string.day_editor_item_break_start, Key.DayEditorBreakStartDone, Key.DayEditorBreakStartHour, Key
            .DayEditorBreakStartMinute),
    BreakEnd(R.string.day_editor_item_break_end, Key.DayEditorBreakEndDone, Key.DayEditorBreakEndHour, Key
            .DayEditorBreakEndMinute),
    End(R.string.day_editor_item_end, Key.DayEditorEndDone, Key.DayEditorEndHour, Key.DayEditorEndMinute);

    private final String name;
    private final Key dayEditorDoneKey;
    private final Key dayEditorHourKey;
    private final Key dayEditorMinuteKey;

    DayEditorItemState(@StringRes int nameResId, Key dayEditorDoneKey, Key dayEditorHourKey, Key dayEditorMinuteKey) {
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
}
