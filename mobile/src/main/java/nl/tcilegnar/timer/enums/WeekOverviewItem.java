package nl.tcilegnar.timer.enums;

import android.support.annotation.Nullable;

import java.util.Calendar;

import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.TimerCalendar;

public enum WeekOverviewItem {
    MONDAY(Calendar.MONDAY),
    TUESDAY(Calendar.TUESDAY),
    WEDNESDAY(Calendar.WEDNESDAY),
    THURSDAY(Calendar.THURSDAY),
    FRIDAY(Calendar.FRIDAY),
    SATURDAY(Calendar.SATURDAY),
    SUNDAY(Calendar.SUNDAY);

    private final int dayOfWeekNumberFromCalendar;
    private final String dayOfWeekText;
    private CurrentDayMillis currentDayMillis;
    private Integer weekNumber;

    WeekOverviewItem(int dayOfWeekNumberFromCalendar) {
        this.dayOfWeekNumberFromCalendar = dayOfWeekNumberFromCalendar;
        this.dayOfWeekText = TimerCalendar.getDayOfWeekTextShort(dayOfWeekNumberFromCalendar);
    }

    public int getDayOfWeekNumberFromCalendar() {
        return dayOfWeekNumberFromCalendar;
    }

    public String getDayOfWeekText() {
        return dayOfWeekText;
    }

    public CurrentDayMillis getCurrentDayMillis() {
        return currentDayMillis;
    }

    public void setCurrentDayMillis(CurrentDayMillis currentDayMillis) {
        this.currentDayMillis = currentDayMillis;
    }

    public String getTotalTimeString() {
        return getCurrentDayMillis().getTotalTimeReadableString();
    }

    @Nullable
    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }
}
