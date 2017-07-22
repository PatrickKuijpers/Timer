package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import nl.tcilegnar.timer.models.WeekOfYear;
import nl.tcilegnar.timer.models.Year;
import nl.tcilegnar.timer.views.viewholders.YearOverviewViewHolder;

public class YearOverviewAdapter extends BaseArrayAdapter<WeekOfYear, YearOverviewViewHolder> {
    public YearOverviewAdapter(Context activityContext, Year year) {
        super(activityContext, getItems(year));
    }

    @NonNull
    private static List<WeekOfYear> getItems(Year year) {
        return year.getAllWeeksOfYear();
    }

    @Override
    protected YearOverviewViewHolder getNewView(Context activityContext) {
        return new YearOverviewViewHolder(activityContext);
    }
}
