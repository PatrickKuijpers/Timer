package nl.tcilegnar.timer.utils.storage;

import nl.tcilegnar.timer.enums.DayEditorItem;

public class Storage extends SharedPrefs {
    private static final int FALSE = 0;

    @Override
    protected String fileName() {
        // Deze filelocatie nooit veranderen!
        return "storage";
    }

    public enum Key {
        // Deze enums nooit veranderen!
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
        DayEditorEndDone(FALSE);

        public final int defaultValue;

        Key(int defaultValue) {
            this.defaultValue = defaultValue;
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
}
