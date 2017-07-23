package nl.tcilegnar.timer.models;

import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.DateFormatter;
import nl.tcilegnar.timer.utils.TimerCalendar;

import static nl.tcilegnar.timer.utils.DateFormatter.DATE_FORMAT_DASHES_1_JAN;

public class WeekOfYear {
    private final Calendar firstDayOfWeek;
    private final List<CurrentDayMillis> dayMillisOfWeek;

    public WeekOfYear(Calendar firstDayOfWeek, List<CurrentDayMillis> dayMillisOfWeek) {
        validateWeekOfYear(firstDayOfWeek, dayMillisOfWeek);
        this.firstDayOfWeek = firstDayOfWeek;
        this.dayMillisOfWeek = dayMillisOfWeek;
    }

    private void validateWeekOfYear(Calendar firstDayOfWeek, List<CurrentDayMillis> dayMillisOfWeek) {
        int requiredWeekNumber = firstDayOfWeek.get(Calendar.WEEK_OF_YEAR);
        Integer previousYearNumber = null;
        for (CurrentDayMillis dayMillis : dayMillisOfWeek) {
            int weekNumber = dayMillis.getDay().get(Calendar.WEEK_OF_YEAR);
            int yearNumber = dayMillis.getDay().get(Calendar.YEAR);
            if (weekNumber != requiredWeekNumber) {
                throw new IllegalArgumentException("Not all weekDays are from the same week(number)");
            } else if (previousYearNumber != null && yearNumber != previousYearNumber) {
                throw new IllegalArgumentException("Not all weekDays are from the same year(number)");
            }
            previousYearNumber = yearNumber;
        }
    }

    public Calendar getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public Calendar getLastDayOfWeek() {
        return TimerCalendar.getLastDayOfWeek(firstDayOfWeek);
    }

    public List<CurrentDayMillis> getDayMillisOfWeek() {
        return dayMillisOfWeek;
    }

    public int getWeekNumber() {
        return firstDayOfWeek.get(Calendar.WEEK_OF_YEAR);
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

    public String getReadablePeriodOfWeek() {
        String firstDayOfWeekText = getReadableStartOfWeek();
        String lastDayOfWeekText = DateFormatter.format(getLastDayOfWeek(), DATE_FORMAT_DASHES_1_JAN);
        return firstDayOfWeekText + " t/m " + lastDayOfWeekText;
    }

    public String getReadableStartOfWeek() {
        return DateFormatter.format(getFirstDayOfWeek(), DATE_FORMAT_DASHES_1_JAN);
    }
}
