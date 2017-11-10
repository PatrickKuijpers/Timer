package nl.tcilegnar.timer.utils.storage;

import java.util.Calendar;

import nl.tcilegnar.timer.enums.DayEditorItemState;
import nl.tcilegnar.timer.models.DayEditorItem;
import nl.tcilegnar.timer.utils.TimerCalendar;

public class Storage extends SharedPrefs {
    private static final int FALSE = 0;
    // TODO: make a 5th state, but this state should not be shown in the view (don't init it when getItemsForAllStates)
    public static final DayEditorItemState NO_ACTIVE_DAY_EDITOR = null;
    //    public static final TodayEditorItem NO_ACTIVE_TODAY_EDITOR = null;

    @Override
    protected String fileName() {
        // Deze filelocatie nooit veranderen!
        return "storage";
    }

    public enum Key {
        // Deze enums nooit veranderen!
        TodayEditorCurrentDate(0),
        TodayEditorStartHour(DayEditorItem.DEFAULT_HOUR_VALUE),
        TodayEditorStartMinute(DayEditorItem.DEFAULT_MINUTE_VALUE),
        TodayEditorStartDone(FALSE),
        TodayEditorBreakStartHour(DayEditorItem.DEFAULT_HOUR_VALUE),
        TodayEditorBreakStartMinute(DayEditorItem.DEFAULT_MINUTE_VALUE),
        TodayEditorBreakStartDone(FALSE),
        TodayEditorBreakEndHour(DayEditorItem.DEFAULT_HOUR_VALUE),
        TodayEditorBreakEndMinute(DayEditorItem.DEFAULT_MINUTE_VALUE),
        TodayEditorBreakEndDone(FALSE),
        TodayEditorEndHour(DayEditorItem.DEFAULT_HOUR_VALUE),
        TodayEditorEndMinute(DayEditorItem.DEFAULT_MINUTE_VALUE),
        TodayEditorEndDone(FALSE),
        ActiveTodayEditor(0),
        DayEditorStartHour(DayEditorItem.DEFAULT_HOUR_VALUE),
        DayEditorStartMinute(DayEditorItem.DEFAULT_MINUTE_VALUE),
        DayEditorStartDone(FALSE),
        DayEditorBreakStartHour(DayEditorItem.DEFAULT_HOUR_VALUE),
        DayEditorBreakStartMinute(DayEditorItem.DEFAULT_MINUTE_VALUE),
        DayEditorBreakStartDone(FALSE),
        DayEditorBreakEndHour(DayEditorItem.DEFAULT_HOUR_VALUE),
        DayEditorBreakEndMinute(DayEditorItem.DEFAULT_MINUTE_VALUE),
        DayEditorBreakEndDone(FALSE),
        DayEditorEndHour(DayEditorItem.DEFAULT_HOUR_VALUE),
        DayEditorEndMinute(DayEditorItem.DEFAULT_MINUTE_VALUE),
        DayEditorEndDone(FALSE),
        ActiveDayEditor(0);

        public final int defaultValue;

        Key(int defaultValue) {
            this.defaultValue = defaultValue;
        }
    }

    public void saveTodayEditorCurrentDate(Calendar calendar) {
        long value = calendar.getTimeInMillis();
        save(Key.TodayEditorCurrentDate.name(), value);
    }

    public Calendar loadTodayEditorCurrentDate() {
        Key key = Key.TodayEditorCurrentDate;
        long value = loadLong(key.name(), key.defaultValue);
        return TimerCalendar.getCalendarInMillis(value);
    }

    /**
     * In tegenstelling tot andere save & load methoden is bij deze de key als extra parameter vereist. Dit is gedaan om
     * te voorkomen dat voor elke DayEditorItem een aparte methode gedefinieerd moet worden
     */
    public void saveDayEditorHour(Key key, int value) {
        save(key.name(), value);
    }

    public int loadDayEditorHour(Key key) {
        return loadInt(key.name(), key.defaultValue);
    }

    public void saveDayEditorMinute(Key key, int value) {
        save(key.name(), value);
    }

    public int loadDayEditorMinute(Key key) {
        return loadInt(key.name(), key.defaultValue);
    }

    public void saveIsDayEditorDone(Key key, boolean isDone) {
        save(key.name(), isDone);
    }

    public boolean loadIsDayEditorDone(Key key) {
        boolean defaultValue = key.defaultValue != FALSE;
        return loadBoolean(key.name(), defaultValue);
    }

    public void saveActiveDayEditor(DayEditorItemState dayEditorItemState) {
        String key = Key.ActiveDayEditor.name();
        save(key, dayEditorItemState.name());
    }

    public DayEditorItemState loadActiveDayEditorState() {
        String key = Key.ActiveDayEditor.name();
        String dayEditorItemName = loadString(key);
        for (DayEditorItemState dayEditorItemState : DayEditorItemState.values()) {
            if (dayEditorItemState.name().equals(dayEditorItemName)) {
                return dayEditorItemState;
            }
        }
        return NO_ACTIVE_DAY_EDITOR;
    }

    //    public void saveActiveTodayEditor(TodayEditorItem todayEditorItem) {
    //        String key = Key.ActiveTodayEditor.name();
    //        save(key, todayEditorItem.name());
    //    }
    //
    //    public TodayEditorItem loadActiveTodayEditor() {
    //        String key = Key.ActiveTodayEditor.name();
    //        String todayEditorItemName = loadString(key);
    //        for (TodayEditorItem todayEditorItem : TodayEditorItem.values()) {
    //            if (todayEditorItem.name().equals(todayEditorItemName)) {
    //                return todayEditorItem;
    //            }
    //        }
    //        return NO_ACTIVE_TODAY_EDITOR;
    //    }

    public void deleteActiveDayEditor() {
        String key = Key.ActiveDayEditor.name();
        save(key, "none");
    }

    public void deleteActiveTodayEditor() {
        String key = Key.ActiveTodayEditor.name();
        save(key, "none");
    }
}
