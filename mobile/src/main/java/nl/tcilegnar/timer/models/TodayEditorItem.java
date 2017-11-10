package nl.tcilegnar.timer.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.enums.DayEditorItemState;
import nl.tcilegnar.timer.interfaces.IDayEditorItem;
import nl.tcilegnar.timer.utils.storage.Storage;

public class TodayEditorItem extends BaseDayEditorItem {
    private final Storage storage = new Storage();

    private TodayEditorItem(DayEditorItemState state) {
        super(state);
    }

    public static List<IDayEditorItem> getItemsForAllStates() {
        List<IDayEditorItem> dayEditorItemsForAllStates = new ArrayList<>();
        for (DayEditorItemState state : DayEditorItemState.values()) {
            dayEditorItemsForAllStates.add(getInstance(state));
        }
        return dayEditorItemsForAllStates;
    }

    public static TodayEditorItem getInstance(DayEditorItemState state) {
        return new TodayEditorItem(state);
    }

    @Override
    public boolean isDone() {
        return storage.loadIsTodayEditorDone(state);
    }

    @Override
    public void setIsDone(boolean isDone) {
        storage.saveIsTodayEditorDone(state, isDone);
    }

    public boolean isActive() {
        DayEditorItemState todayEditorItemState = storage.loadActiveTodayEditorItemState();
        return state.equals(todayEditorItemState);
    }

    public void setActive() {
        storage.saveActiveTodayEditorItemState(state);
    }

    public int getHour() {
        return storage.loadTodayEditorHour(getState());
    }

    public int getMinute() {
        return storage.loadTodayEditorMinute(state);
    }

    public void setCurrentTime(Calendar currentTime) {
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        setCurrentTime(hour, minute);
    }

    public void setCurrentTime(int hour, int minute) {
        storage.saveTodayEditorHour(state, hour);
        storage.saveTodayEditorMinute(state, minute);
    }
}
