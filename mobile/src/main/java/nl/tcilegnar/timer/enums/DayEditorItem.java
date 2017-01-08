package nl.tcilegnar.timer.enums;

import static nl.tcilegnar.timer.utils.storage.Storage.Key;

public enum DayEditorItem {
    Start(Key.DayEditorStartHour, Key.DayEditorStartMinute),
    Break(Key.DayEditorBreakHour, Key.DayEditorBreakMinute),
    BreakEnd(Key.DayEditorBreakEndHour, Key.DayEditorBreakEndMinute),
    End(Key.DayEditorEndHour, Key.DayEditorEndMinute);

    public static final int DEFAULT_HOUR_VALUE = 0;
    public static final int DEFAULT_MINUTE_VALUE = 0;

    private final Key dayEditorHour;
    private final Key dayEditorMinute;

    DayEditorItem(Key dayEditorHour, Key dayEditorMinute) {
        this.dayEditorHour = dayEditorHour;
        this.dayEditorMinute = dayEditorMinute;
    }

    public Key getDayEditorHourKey() {
        return dayEditorHour;
    }

    public Key getDayEditorMinuteKey() {
        return dayEditorMinute;
    }
}
