package nl.tcilegnar.timer.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.utils.Log;
import nl.tcilegnar.timer.utils.TimerCalendar;

public class Year {
    private final String TAG = Log.getTag(this);

    private final int yearNumber;
    private final List<WeekOfYear> allWeeksOfYear;

    public Year(Calendar someDateFromYear, List<CurrentDayMillis> currentDayMillisOfYear) {
        int yearNumber = someDateFromYear.get(Calendar.YEAR);
        List<WeekOfYear> allWeeksOfYear = initiateWeeksOfYear(someDateFromYear, currentDayMillisOfYear);
        this.yearNumber = yearNumber;
        this.allWeeksOfYear = allWeeksOfYear;
    }

    @NonNull
    private List<WeekOfYear> initiateWeeksOfYear(Calendar someDateFromYear, List<CurrentDayMillis> dayMillisOfYear) {
        Log.i(TAG, "initiateWeeksOfYear");
        List<WeekOfYear> allWeeksOfYear = new ArrayList<>();
        // Start 1st of january as firstDayOfWeek, regardless which day of the week it is.
        Calendar firstDayOfWeek = TimerCalendar.getFirstDayOfYear(someDateFromYear);
        Log.d(TAG, "firstDayOfWeek = " + firstDayOfWeek.getTime());
        int requiredYear = someDateFromYear.get(Calendar.YEAR);
        while (firstDayOfWeek.get(Calendar.YEAR) == requiredYear) {
            Calendar firstDayOfNextWeek = TimerCalendar.getFirstDayOfNextWeek(firstDayOfWeek);
            Log.v(TAG, "firstDayOfNextWeek = " + firstDayOfNextWeek.getTime());
            boolean isLastWeek = firstDayOfNextWeek.get(Calendar.YEAR) != requiredYear;
            List<CurrentDayMillis> dayMillisOfWeek = new ArrayList<>();
            for (CurrentDayMillis dayMillis : dayMillisOfYear) {
                // TODO: loop can skip lots of dayMillis when looped through in correct sequence?
                Calendar day = dayMillis.getDay();
                boolean isFirstDayOfWeek = day.getTimeInMillis() == firstDayOfWeek.getTimeInMillis();
                boolean isOtherDayOfWeek = day.after(firstDayOfWeek) && day.before(firstDayOfNextWeek);
                boolean isDayMillisOfOtherYear = isLastWeek && dayMillis.getDay().get(Calendar.YEAR) != requiredYear;
                if ((isFirstDayOfWeek || isOtherDayOfWeek)) {
                    if (!isDayMillisOfOtherYear) {
                        dayMillisOfWeek.add(dayMillis);
                        Log.d(TAG, "added dayMillis: " + dayMillis);
                    } else {
                        Log.w(TAG, "not added dayMillis: " + dayMillis);
                    }
                }
            }
            Calendar firstDayOfWeekForWeekOfYear = TimerCalendar.getCopyOfCalendar(firstDayOfWeek);
            WeekOfYear weekOfYear = new WeekOfYear(firstDayOfWeekForWeekOfYear, dayMillisOfWeek);
            allWeeksOfYear.add(weekOfYear);
            Log.d(TAG, "added weekOfYear: " + weekOfYear.toString());

            for (CurrentDayMillis dayMillisAdded : dayMillisOfWeek) {
                dayMillisOfYear.remove(dayMillisAdded);
            }
            if (!dayMillisOfWeek.isEmpty()) {
                Log.v(TAG, "removed from dayMillisOfYear? New size: " + dayMillisOfYear.size());
            }

            firstDayOfWeek = firstDayOfNextWeek;
        }

        return allWeeksOfYear;
    }

    public int getYearNumber() {
        return yearNumber;
    }

    public List<WeekOfYear> getAllWeeksOfYear() {
        return allWeeksOfYear;
    }

    public int getTotalTimeInMinutes() {
        int totalTimeMinutes = 0;
        for (WeekOfYear weekOfYear : allWeeksOfYear) {
            totalTimeMinutes += weekOfYear.getTotalTimeInMinutes();
        }
        return totalTimeMinutes;
    }

    @Override
    public String toString() {
        return "Year: " + getYearNumber() + ", TotalTimeInMinutes=" + getTotalTimeInMinutes();
    }
}
