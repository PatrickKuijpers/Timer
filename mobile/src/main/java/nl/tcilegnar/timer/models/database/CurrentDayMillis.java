package nl.tcilegnar.timer.models.database;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.enums.DayEditorItemState;
import nl.tcilegnar.timer.interfaces.IDayEditorItem;
import nl.tcilegnar.timer.interfaces.IDayEditorItem.TimeNotSetException;
import nl.tcilegnar.timer.models.DayEditorItem;
import nl.tcilegnar.timer.models.TodayEditorItem;
import nl.tcilegnar.timer.models.Validation;
import nl.tcilegnar.timer.utils.TimerCalendar;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;

import static nl.tcilegnar.timer.enums.DayEditorItemState.BreakEnd;
import static nl.tcilegnar.timer.enums.DayEditorItemState.BreakStart;
import static nl.tcilegnar.timer.enums.DayEditorItemState.End;
import static nl.tcilegnar.timer.enums.DayEditorItemState.Start;
import static nl.tcilegnar.timer.utils.TimerCalendarUtil.areSameDay;

/**
 * {@link CurrentDayMillis} contains a date and several times within that same date. These times should always be in
 * chronological order
 * <p>
 * Assumptions for {@link #timesInMillis}:
 * 1) These times are chronological and contains an equal number of values
 * 2) They will always contain times from {@link DayEditorItemState#Start} and {@link DayEditorItemState#End}
 * 3) In between, there might be several times from {@link DayEditorItemState#BreakStart} and {@link DayEditorItemState#BreakEnd})
 * </p>
 */
@Table(name = "CURRENT_DAY_MILLIS")
public class CurrentDayMillis extends SugarRecord {
    @Column(name = "DAY_IN_MILLIS", unique = true)
    private long dayInMillis;
    @Column(name = "TIMES_IN_MILLIS")
    private String timesInMillis;

    @SuppressWarnings("unused")
    public CurrentDayMillis() {
        // Empty constructor required for SugarRecord!
    }

    /**
     * TODO improve this: CurrentDayMillis without timesInMillis (for days that are not today)
     * TODO: probably split in CurrentDayMillis and DayMillis?
     */
    public CurrentDayMillis(long dayInMillis) {
        saveValues(dayInMillis, new ArrayList<Long>());
    }

    /** TODO improve this: CurrentDayMillis with a specific day, where timesInMillis are instantiated (for today) */
    public CurrentDayMillis(Calendar day) throws TimeNotSetException {
        List<Long> timesInMillis = new ArrayList<>();
        List<Calendar> times = initTimes(day);
        for (Calendar time : times) {
            timesInMillis.add(time.getTimeInMillis());
        }
        saveValues(day.getTimeInMillis(), timesInMillis);
    }

    /** TODO: make sure loop in the right order (start > breaks > end), or else calculations & validation will fail! */
    private List<Calendar> initTimes(Calendar day) throws TimeNotSetException {
        List<Calendar> times = new ArrayList<>();
        times.add(TodayEditorItem.getInstance(Start).getCalendarWithTime(day));
        try {
            times.add(TodayEditorItem.getInstance(BreakStart).getCalendarWithTime(day));
            times.add(TodayEditorItem.getInstance(BreakEnd).getCalendarWithTime(day));
        } catch (IDayEditorItem.TimeNotSetException ignored) {
            // No breaks set is no problem
        }
        times.add(TodayEditorItem.getInstance(End).getCalendarWithTime(day));
        return times;
    }

    private void saveValues(long dayInMillis, List<Long> timesInMillis) {
        this.dayInMillis = dayInMillis;
        this.timesInMillis = timesInMillis.toString();
    }

    public long getDayMillis() {
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

    private List<Long> timesInMillisFromString(String rawString) {
        List<Long> timesInMillis = new ArrayList<>();
        boolean isValidRawString = rawString != null && rawString.contains("[") && rawString.contains("]");
        if (isValidRawString) {
            List<String> allTimesAsStrings = Arrays.asList(rawString.substring(1, rawString.length() - 1).split(", "));
            for (String timeAsString : allTimesAsStrings) {
                if (!timeAsString.isEmpty()) {
                    timesInMillis.add(Long.valueOf(timeAsString));
                }
            }
        }
        return timesInMillis;
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

    public String getTotalTimeReadableString() {
        return TimerCalendarUtil.getReadableTimeStringHoursAndMinutes(getTotalTimeInMinutes());
    }

    @Override
    public String toString() {
        return "CurrentDayMillis{" + new Date(dayInMillis) + ", timesInMillis=" + timesInMillis + "} & ID=" + getId()
                + " & totalTime=" + getTotalTimeReadableString();
    }
}
