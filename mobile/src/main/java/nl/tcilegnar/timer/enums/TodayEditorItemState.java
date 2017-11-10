package nl.tcilegnar.timer.enums;

import android.support.annotation.StringRes;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.utils.Res;

import static nl.tcilegnar.timer.utils.storage.Storage.Key;

/** Order of enums should not be changed (due to calculations in for-loops etc)! */
public enum TodayEditorItemState {
    Start(R.string.day_editor_item_start, Key.TodayEditorStartDone, Key.TodayEditorStartHour, Key
            .TodayEditorStartMinute),
    BreakStart(R.string.day_editor_item_break_start, Key.TodayEditorBreakStartDone, Key.TodayEditorBreakStartHour,
            Key.TodayEditorBreakStartMinute),
    BreakEnd(R.string.day_editor_item_break_end, Key.TodayEditorBreakEndDone, Key.TodayEditorBreakEndHour, Key
            .TodayEditorBreakEndMinute),
    End(R.string.day_editor_item_end, Key.TodayEditorEndDone, Key.TodayEditorEndHour, Key.TodayEditorEndMinute);

    private final String name;
    private final Key todayEditorDoneKey;
    private final Key todayEditorHourKey;
    private final Key todayEditorMinuteKey;

    TodayEditorItemState(@StringRes int nameResId, Key todayEditorDoneKey, Key todayEditorHourKey, Key
            todayEditorMinuteKey) {
        this.name = Res.getString(nameResId);
        this.todayEditorDoneKey = todayEditorDoneKey;
        this.todayEditorHourKey = todayEditorHourKey;
        this.todayEditorMinuteKey = todayEditorMinuteKey;
    }

    @Override
    public String toString() {
        return name;
    }

    public Key getTodayEditorDoneKey() {
        return todayEditorDoneKey;
    }

    public Key getTodayEditorHourKey() {
        return todayEditorHourKey;
    }

    public Key getTodayEditorMinuteKey() {
        return todayEditorMinuteKey;
    }
}
