package nl.tcilegnar.timer.models.database;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.models.Validation;
import nl.tcilegnar.timer.utils.TimerCalendar;

import static nl.tcilegnar.timer.utils.TimerCalendarUtil.areSameDay;

public class CurrentDayMillis extends SugarRecord<CurrentDayMillis> {
    private long dayInMillis;
    private String timesInMillis;

    public CurrentDayMillis() {
        // Empty constructor required for SugarRecord!
    }

    public CurrentDayMillis(Calendar day, List<Calendar> times) {
        List<Long> timesInMillis = new ArrayList<>();
        for (Calendar time : times) {
            timesInMillis.add(time.getTimeInMillis());
        }
        saveValues(day.getTimeInMillis(), timesInMillis);
    }

    private void saveValues(long dayInMillis, List<Long> timesInMillis) {
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
        for (long timeMillis : timesInMillisFromString(timesInMillis)) {
            times.add(TimerCalendar.getCalendarInMillis(timeMillis));
        }
        return times;
    }

    public Calendar getStartOfDayTime() {
        return getTimes().get(0);
    }

    public Calendar getEndOfDayTime() {
        List<Calendar> times = getTimes();
        int lastIndex = times.size() - 1;
        return times.get(lastIndex);
    }

    private List<Long> timesInMillisFromString(String stringFrom) {
        List<Long> list = new ArrayList<>();
        List<String> strings = Arrays.asList(stringFrom.substring(1, stringFrom.length() - 1).split(", "));
        for (String tempString : strings) {
            list.add(Long.valueOf(tempString));
        }
        return list;
    }

    public Validation getValidation() {
        Validation validation = new Validation();
        List<Long> timesMillis = getTimesMillis();
        int numberOfTimes = timesMillis.size();
        if (numberOfTimes <= 1 || numberOfTimes % 2 != 0) {
            validation.setErrorMessage(R.string.validation_error_message_not_all_times_set);
        } else if (!allTimesSameAsDay(getTimes(), getDay())) {
            validation.setErrorMessage(R.string.validation_error_message_not_all_times_same_day);
        } else if (!areConsecutiveTimesLater(timesMillis)) {
            validation.setErrorMessage(R.string.validation_error_message_consecutive_times_not_later);
        }
        return validation;
    }

    private boolean allTimesSameAsDay(List<Calendar> times, Calendar day) {
        boolean allTimesSameDay = true;
        for (Calendar time : times) {
            if (!areSameDay(time, day)) {
                allTimesSameDay = false;
                break;
            }
        }
        return allTimesSameDay;
    }

    private boolean areConsecutiveTimesLater(List<Long> timesMillis) {
        boolean areConsecutiveTimesLater = true;
        long previousTimeInMillis = 0;
        for (long timeInMillis : timesMillis) {
            if (timeInMillis < previousTimeInMillis) {
                areConsecutiveTimesLater = false;
                break;
            }
            previousTimeInMillis = timeInMillis;
        }
        return areConsecutiveTimesLater;
    }

    @Override
    public String toString() {
        return "CurrentDayMillis{currentDay=" + new Date(dayInMillis) + ", timesInMillis=" + timesInMillis + '}';
    }
}
