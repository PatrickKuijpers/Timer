package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.enums.WeekOverviewItem;
import nl.tcilegnar.timer.models.Week;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.views.viewholders.YearOverviewViewHolder;

public class YearOverviewAdapter extends BaseArrayAdapter<Week, YearOverviewViewHolder> {
    public YearOverviewAdapter(Context activityContext, List<CurrentDayMillis> currentDayMillisOfYear) {
        super(activityContext, getItems(currentDayMillisOfYear));
    }

    @NonNull
    private static List<Week> getItems(List<CurrentDayMillis> currentDayMillisOfYear) {
        List<Week> weeks = new ArrayList<>();
        //        for (CurrentDayMillis currentDayMillis : currentDayMillisOfYear) {
        //            int weekNumber = currentDayMillis.getDay().get(Calendar.WEEK_OF_YEAR);
        //            List<WeekOverviewItem> week = weeks.get(weekNumber);
        //            if (week == null) {
        //                week = new ArrayList<>();
        //                for (WeekOverviewItem day : WeekOverviewItem.values()) {
        //                    week.add(day.getDayOfWeekNumberFromCalendar(), day);
        //                }
        //            }
        //            for (WeekOverviewItem day : week) {
        //                int dayOfWeekNumber = day.getDayOfWeekNumberFromCalendar();
        //                int dayOfWeekNumberCurrentDayMillis = currentDayMillis.getDay().get(Calendar.DAY_OF_WEEK);
        //                if (dayOfWeekNumber == dayOfWeekNumberCurrentDayMillis) {
        //                    week.set(dayOfWeekNumber, weekItem);
        //                    break;
        //                }
        //            }
        //            WeekOverviewItem weekItem = week.get(dayOfWeekNumber);
        //            weekItem.setCurrentDayMillis(currentDayMillis);
        //            weeks.put(weekNumber, week);
        //            for (List<WeekOverviewItem> week : weeks) {
        //                Integer weekNumber = week.get(0).getWeekNumber();
        //                if (weekNumber)
        //            }
        //        }
        //    }

        // weekOverviewItems = Arrays.asList(WeekOverviewItem.values());
        //                for(
        //    WeekOverviewItem weekOverviewItem :weekOverviewItems)
        //
        //    {
        //        CurrentDayMillis currentDayMillis = getCurrentDayMillis(weekOverviewItem, currentDayMillisOfYear);
        //        weekOverviewItem.setCurrentDayMillis(currentDayMillis);
        //    }
        return weeks;
    }

    @NonNull
    private static CurrentDayMillis getCurrentDayMillis(WeekOverviewItem requiredWeekOverviewItem,
                                                        List<CurrentDayMillis> currentDayMillisOfYear) {
        int dayOfWeekNumberForRequiredWeekOverViewItem = requiredWeekOverviewItem.getDayOfWeekNumberFromCalendar();
        for (CurrentDayMillis currentDayMillis : currentDayMillisOfYear) {
            int dayOfWeekNumber = currentDayMillis.getDay().get(Calendar.DAY_OF_WEEK);
            if (dayOfWeekNumberForRequiredWeekOverViewItem == dayOfWeekNumber) {
                return currentDayMillis;
            }
        }
        return new CurrentDayMillis(); // No currentDayMillis found: return empty CurrentDayMillis // TODO: correct day?
    }

    @Override
    protected YearOverviewViewHolder getNewView(Context activityContext) {
        return new YearOverviewViewHolder(activityContext);
    }
}
