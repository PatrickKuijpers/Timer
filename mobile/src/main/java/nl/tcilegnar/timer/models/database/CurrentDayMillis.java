package nl.tcilegnar.timer.models.database;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CurrentDayMillis extends SugarRecord<CurrentDayMillis> {
    private Long dayInMillis;
    private List<Long> timesInMillis;

    public CurrentDayMillis() {
    }

    public CurrentDayMillis(Long dayInMillis, List<Long> timesInMillis) {
        this.dayInMillis = dayInMillis;
        this.timesInMillis = timesInMillis;
    }

    public CurrentDayMillis(Calendar day, List<Calendar> times) {
        this.dayInMillis = day.getTimeInMillis();

        List<Long> timesInMillis = new ArrayList<>();
        for (Calendar time : times) {
            timesInMillis.add(time.getTimeInMillis());
        }
        this.timesInMillis = timesInMillis;
    }

    public Long getDayMillis() {
        return dayInMillis;
    }

    public Calendar getDay() {
        return getCalendar(dayInMillis);
    }

    public List<Long> getTimesMillis() {
        return timesInMillis;
    }

    public List<Calendar> getTimes() {
        List<Calendar> times = new ArrayList<>();
        for (Long timeMillis : timesInMillis) {
            times.add(getCalendar(timeMillis));
        }
        return times;
    }

    private Calendar getCalendar(Long timeInMillis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);
        return cal;
    }

    @Override
    public String toString() {
        return "CurrentDayMillis{currentDay=" + new Date(dayInMillis) + ", timesInMillis=" + timesInMillis + '}';
    }
}
