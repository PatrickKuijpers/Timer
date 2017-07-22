package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import nl.tcilegnar.timer.enums.WeekOverviewItem;
import nl.tcilegnar.timer.models.Week;
import nl.tcilegnar.timer.views.viewholders.WeekOverviewViewHolder;

public class WeekOverviewAdapter extends BaseArrayAdapter<WeekOverviewItem, WeekOverviewViewHolder> {
    public WeekOverviewAdapter(Context activityContext, Week week) {
        super(activityContext, getItems(week));
    }

    @NonNull
    private static List<WeekOverviewItem> getItems(Week week) {
        return week.getWeekDays();
    }

    @Override
    protected WeekOverviewViewHolder getNewView(Context activityContext) {
        return new WeekOverviewViewHolder(activityContext);
    }
}
