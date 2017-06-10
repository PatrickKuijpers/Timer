package nl.tcilegnar.timer.utils.storage;

import java.util.Calendar;

import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.utils.TimerCalendar;

public class Storage extends SharedPrefs {
    private static final int FALSE = 0;
    private static final DayEditorItem NO_ACTIVE_DAY_EDITOR = null;

    @Override
    protected String fileName() {
        // Deze filelocatie nooit veranderen!
        return "storage";
    }

    public enum Key {
        // Deze enums nooit veranderen!
        DayEditorCurrentDay(0),
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

    public void saveDayEditorCurrentDay(Calendar calendar) {
        long value = calendar.getTimeInMillis();
        save(Key.DayEditorCurrentDay.name(), value);
    }

    public Calendar loadDayEditorCurrentDay() {
        if (loadActiveDayEditor() == NO_ACTIVE_DAY_EDITOR) {
            // No day editor active = no time set: assume you'd like to start over with a new day instead of retreiving
            return TimerCalendar.getCurrentDate();
        } else {
            Key key = Key.DayEditorCurrentDay;
            long value = loadLong(key.name(), key.defaultValue);
            return TimerCalendar.getCalendarInMillis(value);
        }
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

    public void saveActiveDayEditor(DayEditorItem dayEditorItem) {
        String key = Key.ActiveDayEditor.name();
        save(key, dayEditorItem.name());
    }

    public DayEditorItem loadActiveDayEditor() {
        String key = Key.ActiveDayEditor.name();
        String dayEditorItemName = loadString(key);
        for (DayEditorItem dayEditorItem : DayEditorItem.values()) {
            if (dayEditorItem.name().equals(dayEditorItemName)) {
                return dayEditorItem;
            }
        }
        return NO_ACTIVE_DAY_EDITOR;
    }

    public void deleteActiveDayEditor() {
        String key = Key.ActiveDayEditor.name();
        save(key, "none");
    }
}
