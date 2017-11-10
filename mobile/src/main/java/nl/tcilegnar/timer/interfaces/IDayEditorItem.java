package nl.tcilegnar.timer.interfaces;

import java.util.Calendar;

import nl.tcilegnar.timer.enums.DayEditorItemState;

public interface IDayEditorItem {
    DayEditorItemState getState();

    boolean isDone();

    void setIsDone(boolean isDone);

    boolean isActive();

    void setActive();

    boolean isEnabled();

    void enable();

    void disable();

    Calendar getCalendarWithTime(Calendar currentDate) throws TimeNotSetException;

    int getHour();

    int getMinute();

    void setCurrentTime(Calendar currentTime);

    void setCurrentTime(int hour, int minute);

    class TimeNotSetException extends Exception {
        public TimeNotSetException() {
            super("Time is not set, so cannot be used to calculate time or create a Calendar instance");
        }
    }
}
