package nl.tcilegnar.timer.models;

import java.util.ArrayList;
import java.util.List;

import nl.tcilegnar.timer.enums.DayEditorItemState;
import nl.tcilegnar.timer.interfaces.IDayEditorItem;

public class DayEditorItem extends BaseDayEditorItem {
    private int hour = DEFAULT_HOUR_VALUE;
    private int minute = DEFAULT_MINUTE_VALUE;
    private boolean isDone;

    private DayEditorItem(DayEditorItemState state) {
        super(state);
    }

    public static List<IDayEditorItem> getItemsForAllStates() {
        List<IDayEditorItem> dayEditorItemsForAllStates = new ArrayList<>();
        for (DayEditorItemState state : DayEditorItemState.values()) {
            dayEditorItemsForAllStates.add(getInstance(state));
        }
        return dayEditorItemsForAllStates;
    }

    public static DayEditorItem getInstance(DayEditorItemState state) {
        return new DayEditorItem(state);
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isActive() {
        return getState() == ActiveDayEditorItem.INSTANCE.getActiveState();
    }

    public void setActive() {
        ActiveDayEditorItem.INSTANCE.setActiveState(getState());
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setCurrentTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }
}
