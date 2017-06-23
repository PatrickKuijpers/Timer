package nl.tcilegnar.timer.adapters;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import nl.tcilegnar.timer.enums.WeekOverviewItem;
import nl.tcilegnar.timer.views.DayEditorItemView;
import nl.tcilegnar.timer.views.viewholders.WeekOverviewViewHolder;

public class WeekOverviewAdapter extends BaseArrayAdapter<WeekOverviewItem, WeekOverviewViewHolder> {
    public WeekOverviewAdapter(Context activityContext) {
        super(activityContext, Arrays.asList(WeekOverviewItem.values()));
    }

    @Override
    protected WeekOverviewViewHolder getNewView(Context activityContext) {
        return new WeekOverviewViewHolder(activityContext);
    }
}
