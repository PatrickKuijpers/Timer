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
    private CurrentDayMillis dayMillis;

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

    public CurrentDayMillis getDayMillis() {
        return dayMillis;
    }

    public void setDayMillis(CurrentDayMillis dayMillis) {
        this.dayMillis = dayMillis;
    }

    public String getTotalTimeString() {
        return getDayMillis().getTotalTimeReadableString();
    }

    @Nullable
    public Integer getWeekNumber() {
        return dayMillis != null && dayMillis.getDay() != null ? dayMillis.getDay().get(Calendar.WEEK_OF_YEAR) : null;
    }
}
