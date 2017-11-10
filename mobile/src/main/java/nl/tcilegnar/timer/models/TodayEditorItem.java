package nl.tcilegnar.timer.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.enums.TodayEditorItemState;
import nl.tcilegnar.timer.interfaces.IDayEditorItem;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.storage.Storage;

public class TodayEditorItem extends BaseDayEditorItem<TodayEditorItemState> {
    private final Storage storage = new Storage();

    private TodayEditorItem(TodayEditorItemState state) {
        super(state);
    }

    public static List<IDayEditorItem> getItemsForAllStates() {
        List<IDayEditorItem> dayEditorItemsForAllStates = new ArrayList<>();
        for (TodayEditorItemState state : TodayEditorItemState.values()) {
            dayEditorItemsForAllStates.add(new TodayEditorItem(state));
        }
        return dayEditorItemsForAllStates;
    }

    public static TodayEditorItem get(TodayEditorItemState state) {
        return new TodayEditorItem(state);
    }

    @Override
    public boolean isDone() {
        return storage.loadIsTodayEditorDone(state.getTodayEditorDoneKey());
    }

    @Override
    public void setIsDone(boolean isDone) {
        storage.saveIsTodayEditorDone(state.getTodayEditorDoneKey(), isDone);
    }

    public boolean isActive() {
        TodayEditorItemState todayEditorItemState = storage.loadActiveTodayEditorItemState();
        return getState().equals(todayEditorItemState);
    }

    public void setActive() {
        storage.saveActiveTodayEditorItemState(getState());
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
        return storage.loadTodayEditorHour(state.getTodayEditorHourKey());
    }

    public int getMinute() {
        return storage.loadTodayEditorMinute(state.getTodayEditorMinuteKey());
    }

    public void setCurrentTime(Calendar currentTime) {
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        setCurrentTime(hour, minute);
    }

    public void setCurrentTime(int hour, int minute) {
        storage.saveTodayEditorHour(state.getTodayEditorHourKey(), hour);
        storage.saveTodayEditorMinute(state.getTodayEditorMinuteKey(), minute);
    }
}
