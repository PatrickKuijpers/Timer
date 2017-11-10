package nl.tcilegnar.timer.models;

import java.util.Calendar;

import nl.tcilegnar.timer.interfaces.IDayEditorItem;
import nl.tcilegnar.timer.utils.TimerCalendar;

import static nl.tcilegnar.timer.views.DayEditorItemView.NO_TIME;

public abstract class BaseDayEditorItem<E extends Enum> implements IDayEditorItem {
    public static final int DEFAULT_HOUR_VALUE = NO_TIME;
    public static final int DEFAULT_MINUTE_VALUE = NO_TIME;

    protected final E state;

    private boolean enabled = true;

    protected BaseDayEditorItem(E state) {
        this.state = state;
    }

    @Override
    public E getState() {
        return state;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
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

    public void setCurrentTime(Calendar currentTime) {
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        setCurrentTime(hour, minute);
    }

    public abstract void setCurrentTime(int hour, int minute);
}
