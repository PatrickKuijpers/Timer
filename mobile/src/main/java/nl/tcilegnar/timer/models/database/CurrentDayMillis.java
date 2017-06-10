package nl.tcilegnar.timer.models.database;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.models.Validation;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;

import static nl.tcilegnar.timer.utils.TimerCalendarUtil.areSameDay;

/**
 * {@link CurrentDayMillis} contains a date and several times within that same date. These times should always be in
 * chronological order
 * <p>
 * Assumptions for {@link #timesInMillis}:
 * 1) These times are chronological and contains an equal number of values
 * 2) They will always contain times from {@link DayEditorItem#Start} and {@link DayEditorItem#End}
 * 3) In between, there might be several times from {@link DayEditorItem#BreakStart} and {@link DayEditorItem#BreakEnd})
 * </p>
 */
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
        // TODO: is ordering of times necessary here?
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

    public int getTotalTimeInMinutes() {
        int totalTimeInMinutes = 0;
        List<Calendar> times = getTimes();
        for (int i = 1; i < times.size(); i += 2) {     // Loop through all times
            Calendar previousTime = times.get(i - 1);   // Previous time (start of day or end of a break)
            Calendar time = times.get(i);               // Time to compare to (start of a break or end of day)

            // Calculate difference of these times (so never between breaks) and add to total time
            totalTimeInMinutes += TimerCalendarUtil.getDateDiff(previousTime, time, TimeUnit.MINUTES);
        }
        return totalTimeInMinutes;
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
        } else if (getTotalTimeInMinutes() < 0) {
            validation.setErrorMessage(R.string.validation_error_message_total_time_negative);
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
