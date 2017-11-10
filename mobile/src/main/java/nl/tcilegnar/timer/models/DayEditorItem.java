package nl.tcilegnar.timer.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.enums.DayEditorItemState;
import nl.tcilegnar.timer.interfaces.IDayEditorItem;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.storage.Storage;

import static nl.tcilegnar.timer.views.DayEditorItemView.NO_TIME;

/** TODO: no storage needed for DayEditor, only TodayEditor? */
public class DayEditorItem implements IDayEditorItem {
    public static final int DEFAULT_HOUR_VALUE = NO_TIME;
    public static final int DEFAULT_MINUTE_VALUE = NO_TIME;

    private final DayEditorItemState state;
    private final Storage storage = new Storage();

    private boolean enabled = true;

    private DayEditorItem(DayEditorItemState state) {
        this.state = state;
    }

    public static List<IDayEditorItem> getItemsForAllStates() {
        List<IDayEditorItem> dayEditorItemsForAllStates = new ArrayList<>();
        for (DayEditorItemState state : DayEditorItemState.values()) {
            dayEditorItemsForAllStates.add(new DayEditorItem(state));
        }
        return dayEditorItemsForAllStates;
    }

    public static DayEditorItem get(DayEditorItemState state) {
        return new DayEditorItem(state);
    }

    @Override
    public DayEditorItemState getState() {
        return state;
    }

    @Override
    public boolean isDone() {
        return storage.loadIsDayEditorDone(state.getDayEditorDoneKey());
    }

    @Override
    public void setIsDone(boolean isDone) {
        storage.saveIsDayEditorDone(state.getDayEditorDoneKey(), isDone);
    }

    public boolean isActive() {
        DayEditorItemState dayEditorItemState = storage.loadActiveDayEditorState();
        return getState().equals(dayEditorItemState);
    }

    public void setActive() {
        storage.saveActiveDayEditor(getState());
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
        return storage.loadDayEditorHour(state.getDayEditorHourKey());
    }

    public int getMinute() {
        return storage.loadDayEditorMinute(state.getDayEditorMinuteKey());
    }

    public void setCurrentTime(Calendar currentTime) {
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        setCurrentTime(hour, minute);
    }

    public void setCurrentTime(int hour, int minute) {
        storage.saveDayEditorHour(state.getDayEditorHourKey(), hour);
        storage.saveDayEditorMinute(state.getDayEditorMinuteKey(), minute);
    }
}
