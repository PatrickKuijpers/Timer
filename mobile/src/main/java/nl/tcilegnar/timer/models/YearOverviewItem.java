package nl.tcilegnar.timer.models;

import java.util.ArrayList;
import java.util.List;

import nl.tcilegnar.timer.enums.WeekOverviewItem;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;

public class YearOverviewItem {
    private final int weekNumberFromCalendar;
    private List<WeekOverviewItem> week = new ArrayList<>();
    private Integer totalTimeInMinutes;

    YearOverviewItem(int weekNumberFromCalendar) {
        this.weekNumberFromCalendar = weekNumberFromCalendar;
    }

    public int getWeekNumberFromCalendar() {
        return weekNumberFromCalendar;
    }

    public List<WeekOverviewItem> getWeek() {
        return week;
    }

    public void addWeekItem(WeekOverviewItem weekItem) {
        week.add(weekItem.getDayOfWeekNumberFromCalendar(), weekItem);
    }

    public String getTotalTimeString() {
        if (totalTimeInMinutes == null) {
            totalTimeInMinutes = 0;
            for (WeekOverviewItem day : week) {
                totalTimeInMinutes += day.getDayMillis().getTotalTimeInMinutes();
            }
        }
        return TimerCalendarUtil.getReadableTimeStringHoursAndMinutes(totalTimeInMinutes);
    }
}
