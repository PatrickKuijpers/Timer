package nl.tcilegnar.timer.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.enums.WeekOverviewItem;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.TimerCalendar;

public class Week {
    private final int weekNumber;
    private final List<WeekOverviewItem> weekDays;

    public Week(Calendar someDateFromWeek, List<CurrentDayMillis> currentDayMillisOfWeek) {
        int weekNumber = someDateFromWeek.get(Calendar.WEEK_OF_YEAR);
        List<WeekOverviewItem> weekDays = initiateWeekDays(someDateFromWeek, currentDayMillisOfWeek);
        validateWeek(weekNumber, weekDays);
        this.weekNumber = weekNumber;
        this.weekDays = weekDays;
    }

    @NonNull
    private List<WeekOverviewItem> initiateWeekDays(Calendar someDateFromWeek, List<CurrentDayMillis>
            currentDayMillisOfWeek) {
        List<WeekOverviewItem> weekDays = new ArrayList<>();
        for (WeekOverviewItem weekDay : WeekOverviewItem.values()) {
            CurrentDayMillis dayMillis = getDayMillis(someDateFromWeek, weekDay, currentDayMillisOfWeek);
            weekDay.setDayMillis(dayMillis);
            weekDays.add(weekDay);
        }
        return weekDays;
    }

    @NonNull
    private CurrentDayMillis getDayMillis(Calendar someDateFromWeek, WeekOverviewItem requiredWeekDay,
                                          List<CurrentDayMillis> currentDayMillisOfWeek) {
        int dayOfWeekNumberForRequiredWeekDay = requiredWeekDay.getDayOfWeekNumberFromCalendar();
        for (CurrentDayMillis currentDayMillis : currentDayMillisOfWeek) {
            int dayOfWeekNumber = currentDayMillis.getDay().get(Calendar.DAY_OF_WEEK);
            if (dayOfWeekNumberForRequiredWeekDay == dayOfWeekNumber) {
                return currentDayMillis;
            }
        }
        return getEmptyCurrentDayMillisFor(someDateFromWeek, dayOfWeekNumberForRequiredWeekDay);
    }

    private CurrentDayMillis getEmptyCurrentDayMillisFor(Calendar someDateFromWeek, int dayOfWeekNumber) {
        Calendar cal = TimerCalendar.getCopyOfCalendar(someDateFromWeek);
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeekNumber);
        return new CurrentDayMillis(cal.getTimeInMillis());
    }

    private void validateWeek(Integer weekNumber, List<WeekOverviewItem> weekDays) {
        for (WeekOverviewItem weekDay : weekDays) {
            Integer checkWeekNumber = weekDay.getWeekNumber();
            if (checkWeekNumber == null) {
                throw new IllegalArgumentException("WeekNumber unknown for weekDay " + weekDay.getDayOfWeekText());
            } else if (!checkWeekNumber.equals(weekNumber)) {
                throw new IllegalArgumentException("Not all weekDays are from the same week(number)");
            }
        }
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public List<WeekOverviewItem> getWeekDays() {
        return weekDays;
    }

    public int getTotalTimeInMinutes() {
        int totalTimeMinutes = 0;
        for (WeekOverviewItem weekDay : weekDays) {
            totalTimeMinutes += weekDay.getDayMillis().getTotalTimeInMinutes();
        }
        return totalTimeMinutes;
    }

    @Override
    public String toString() {
        return "Week: " + getWeekNumber() + ", TotalTimeInMinutes=" + getTotalTimeInMinutes();
    }
}
