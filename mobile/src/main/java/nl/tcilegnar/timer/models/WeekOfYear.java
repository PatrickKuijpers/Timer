package nl.tcilegnar.timer.models;

import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.models.database.CurrentDayMillis;

public class WeekOfYear {
    private final int weekNumber;
    private final List<CurrentDayMillis> dayMillisOfWeek;

    public WeekOfYear(Calendar someDateFromWeek, List<CurrentDayMillis> dayMillisOfWeek) {
        int weekNumber = someDateFromWeek.get(Calendar.WEEK_OF_YEAR);
        validateWeekOfYear(weekNumber, dayMillisOfWeek);
        this.weekNumber = weekNumber;
        this.dayMillisOfWeek = dayMillisOfWeek;
    }

    private void validateWeekOfYear(Integer weekNumber, List<CurrentDayMillis> dayMillisOfWeek) {
        Integer previousYearNumber = null;
        for (CurrentDayMillis dayMillis : dayMillisOfWeek) {
            int checkWeekNumber = dayMillis.getDay().get(Calendar.WEEK_OF_YEAR);
            int checkYearNumber = dayMillis.getDay().get(Calendar.YEAR);
            if (checkWeekNumber != weekNumber) {
                throw new IllegalArgumentException("Not all weekDays are from the same week(number)");
            } else if (previousYearNumber != null && checkYearNumber != previousYearNumber) {
                throw new IllegalArgumentException("Not all weekDays are from the same year(number)");
            }
            previousYearNumber = checkYearNumber;
        }
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public List<CurrentDayMillis> getDayMillisOfWeek() {
        return dayMillisOfWeek;
    }

    public int getTotalTimeInMinutes() {
        int totalTimeMinutes = 0;
        for (CurrentDayMillis dayMillis : dayMillisOfWeek) {
            totalTimeMinutes += dayMillis.getTotalTimeInMinutes();
        }
        return totalTimeMinutes;
    }

    @Override
    public String toString() {
        return "Week: " + getWeekNumber() + ", TotalTimeInMinutes=" + getTotalTimeInMinutes();
    }
}
