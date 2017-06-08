package nl.tcilegnar.timer.models.database;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.tcilegnar.timer.utils.TimerCalendar;

public class CurrentDayMillis extends SugarRecord<CurrentDayMillis> {
    private Long dayInMillis;
    private String timesInMillis;

    public CurrentDayMillis() {
    }

    public CurrentDayMillis(Long dayInMillis, List<Long> timesInMillis) {
        saveValues(dayInMillis, timesInMillis);
    }

    public CurrentDayMillis(Calendar day, List<Calendar> times) {
        List<Long> timesInMillis = new ArrayList<>();
        for (Calendar time : times) {
            timesInMillis.add(time.getTimeInMillis());
        }
        saveValues(day.getTimeInMillis(), timesInMillis);
    }

    private void saveValues(Long dayInMillis, List<Long> timesInMillis) {
        this.dayInMillis = dayInMillis;
        this.timesInMillis = timesInMillis.toString();
    }

    public Long getDayMillis() {
        return dayInMillis;
    }

    public Calendar getDay() {
        return TimerCalendar.getCalendarInMillis(dayInMillis);
    }

    public List<Long> getTimesMillis() {
        return timesInMillisFromString(timesInMillis);
    }

    public List<Calendar> getTimes() {
        List<Calendar> times = new ArrayList<>();
        for (Long timeMillis : timesInMillisFromString(timesInMillis)) {
            times.add(TimerCalendar.getCalendarInMillis(timeMillis));
        }
        return times;
    }

    private List<Long> timesInMillisFromString(String stringFrom) {
        List<Long> list = new ArrayList<>();
        List<String> strings = Arrays.asList(stringFrom.substring(1, stringFrom.length() - 1).split(", "));
        for (String tempString : strings) {
            list.add(Long.valueOf(tempString));
        }
        return list;
    }

    @Override
    public String toString() {
        return "CurrentDayMillis{currentDay=" + new Date(dayInMillis) + ", timesInMillis=" + timesInMillis + '}';
    }
}
