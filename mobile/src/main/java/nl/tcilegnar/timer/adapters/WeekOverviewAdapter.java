package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import nl.tcilegnar.timer.enums.WeekOverviewItem;
import nl.tcilegnar.timer.models.database.CurrentDayMillis;
import nl.tcilegnar.timer.views.viewholders.WeekOverviewViewHolder;

public class WeekOverviewAdapter extends BaseArrayAdapter<WeekOverviewItem, WeekOverviewViewHolder> {
    public WeekOverviewAdapter(Context activityContext, List<CurrentDayMillis> currentDayMillisOfWeek) {
        super(activityContext, getItems(currentDayMillisOfWeek));
    }

    @NonNull
    private static List<WeekOverviewItem> getItems(List<CurrentDayMillis> currentDayMillisOfWeek) {
        List<WeekOverviewItem> weekOverviewItems = Arrays.asList(WeekOverviewItem.values());
        for (WeekOverviewItem weekOverviewItem : weekOverviewItems) {
            CurrentDayMillis currentDayMillis = getCurrentDayMillis(weekOverviewItem, currentDayMillisOfWeek);
            weekOverviewItem.setCurrentDayMillis(currentDayMillis);
        }
        return weekOverviewItems;
    }

    @NonNull
    private static CurrentDayMillis getCurrentDayMillis(WeekOverviewItem requiredWeekOverviewItem,
                                                        List<CurrentDayMillis> currentDayMillisOfWeek) {
        int dayOfWeekNumberForRequiredWeekOverViewItem = requiredWeekOverviewItem.getDayOfWeekNumberFromCalendar();
        for (CurrentDayMillis currentDayMillis : currentDayMillisOfWeek) {
            int dayOfWeekNumber = currentDayMillis.getDay().get(Calendar.DAY_OF_WEEK);
            if (dayOfWeekNumberForRequiredWeekOverViewItem == dayOfWeekNumber) {
                return currentDayMillis;
            }
        }
        return new CurrentDayMillis(); // No currentDayMillis found: return empty CurrentDayMillis // TODO: correct day?
    }

    @Override
    protected WeekOverviewViewHolder getNewView(Context activityContext) {
        return new WeekOverviewViewHolder(activityContext);
    }
}
