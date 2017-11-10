package nl.tcilegnar.timer.utils.storage;

import java.util.Calendar;

import nl.tcilegnar.timer.enums.DayEditorItemState;
import nl.tcilegnar.timer.models.DayEditorItem;
import nl.tcilegnar.timer.utils.TimerCalendar;

public class Storage extends SharedPrefs {
    private static final int FALSE = 0;
    // TODO: make a 5th state, but this state should not be shown in the view (don't init it when getItemsForAllStates)
    public static final DayEditorItemState NO_ACTIVE_TODAY_EDITOR = null;

    @Override
    protected String fileName() {
        // Deze filelocatie nooit veranderen!
        return "storage";
    }

    public enum Key {
        // Deze enums nooit veranderen!
        TodayEditorCurrentDate(0),
        TodayEditorHour(DayEditorItem.DEFAULT_HOUR_VALUE),
        TodayEditorMinute(DayEditorItem.DEFAULT_MINUTE_VALUE),
        TodayEditorDone(FALSE),
        ActiveTodayEditorItemState(0);

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
    public void saveTodayEditorHour(DayEditorItemState state, int value) {
        save(getEnumTypedKey(Key.TodayEditorHour, state), value);
    }

    public int loadTodayEditorHour(DayEditorItemState state) {
        Key key = Key.TodayEditorHour;
        return loadInt(getEnumTypedKey(key, state), key.defaultValue);
    }

    public void saveTodayEditorMinute(DayEditorItemState state, int value) {
        save(getEnumTypedKey(Key.TodayEditorMinute, state), value);
    }

    public int loadTodayEditorMinute(DayEditorItemState state) {
        Key key = Key.TodayEditorMinute;
        return loadInt(getEnumTypedKey(key, state), key.defaultValue);
    }

    public void saveIsTodayEditorDone(DayEditorItemState state, boolean isDone) {
        save(getEnumTypedKey(Key.TodayEditorDone, state), isDone);
    }

    public boolean loadIsTodayEditorDone(DayEditorItemState state) {
        Key key = Key.TodayEditorDone;
        boolean defaultValue = key.defaultValue != FALSE;
        return loadBoolean(getEnumTypedKey(key, state), defaultValue);
    }

    public void saveActiveTodayEditorItemState(DayEditorItemState todayEditorItemState) {
        String key = Key.ActiveTodayEditorItemState.name();
        save(key, todayEditorItemState.name());
    }

    public DayEditorItemState loadActiveTodayEditorItemState() {
        String key = Key.ActiveTodayEditorItemState.name();
        String todayEditorItemName = loadString(key);
        for (DayEditorItemState todayEditorItemState : DayEditorItemState.values()) {
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

    private String getEnumTypedKey(Key key, Enum state) {
        return key.name() + "_" + state.name();
    }
}
