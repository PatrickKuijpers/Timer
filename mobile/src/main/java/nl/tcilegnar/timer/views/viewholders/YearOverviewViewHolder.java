package nl.tcilegnar.timer.views.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.models.Week;

public class YearOverviewViewHolder extends BaseViewHolder<Week> {
    private TextView dayOfWeekLabel;
    private TextView dayOfWeekValue;

    public YearOverviewViewHolder(Context activityContext) {
        super(activityContext);
        initViews();
    }

    public YearOverviewViewHolder(Context activityContext, AttributeSet attrs) {
        super(activityContext, attrs);
        initViews();
    }

    private void initViews() {
        dayOfWeekLabel = (TextView) findViewById(R.id.day_of_week_label);
        dayOfWeekValue = (TextView) findViewById(R.id.day_of_week_value);
    }

    @Override
    protected int getViewHolderResource() {
        return R.layout.viewholder_week_overview;
    }

    @Override
    public void loadData(Week weekOverviewItem) {
        dayOfWeekLabel.setText(weekOverviewItem.getWeekNumber());
        dayOfWeekValue.setText(String.valueOf(weekOverviewItem.getTotalTime()));
    }
}
