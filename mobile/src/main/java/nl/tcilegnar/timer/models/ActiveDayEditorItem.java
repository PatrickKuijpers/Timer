package nl.tcilegnar.timer.models;

import nl.tcilegnar.timer.enums.DayEditorItemState;

public enum ActiveDayEditorItem {
    INSTANCE;

    private static DayEditorItemState activeState;

    public DayEditorItemState getActiveState() {
        return activeState;
    }

    public void setActiveState(DayEditorItemState activeState) {
        ActiveDayEditorItem.activeState = activeState;
    }

    public void reset() {
        setActiveState(DayEditorItemState.Start);
    }
}
