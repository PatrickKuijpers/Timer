package nl.tcilegnar.timer.utils.storage;

import java.util.Calendar;

import nl.tcilegnar.timer.enums.TodayEditorItemState;
import nl.tcilegnar.timer.models.DayEditorItem;
import nl.tcilegnar.timer.utils.TimerCalendar;

public class Storage extends SharedPrefs {
    private static final int FALSE = 0;
    // TODO: make a 5th state, but this state should not be shown in the view (don't init it when getItemsForAllStates)
    public static final TodayEditorItemState NO_ACTIVE_TODAY_EDITOR = null;

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
        ActiveTodayEditorItemState(0),
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
    public void saveTodayEditorHour(Key key, int value) {
        save(key.name(), value);
    }

    public int loadTodayEditorHour(Key key) {
        return loadInt(key.name(), key.defaultValue);
    }

    public void saveTodayEditorMinute(Key key, int value) {
        save(key.name(), value);
    }

    public int loadTodayEditorMinute(Key key) {
        return loadInt(key.name(), key.defaultValue);
    }

    public void saveIsTodayEditorDone(Key key, boolean isDone) {
        save(key.name(), isDone);
    }

    public boolean loadIsTodayEditorDone(Key key) {
        boolean defaultValue = key.defaultValue != FALSE;
        return loadBoolean(key.name(), defaultValue);
    }

    public void saveActiveTodayEditorItemState(TodayEditorItemState todayEditorItemState) {
        String key = Key.ActiveTodayEditorItemState.name();
        save(key, todayEditorItemState.name());
    }

    public TodayEditorItemState loadActiveTodayEditorItemState() {
        String key = Key.ActiveTodayEditorItemState.name();
        String todayEditorItemName = loadString(key);
        for (TodayEditorItemState todayEditorItemState : TodayEditorItemState.values()) {
            if (todayEditorItemState.name().equals(todayEditorItemName)) {
                return todayEditorItemState;
            }
        }
        return NO_ACTIVE_TODAY_EDITOR;
    }

    public void deleteActiveTodayEditor() {
        String key = Key.ActiveTodayEditorItemState.name();
        save(key, "none");
    }
}
