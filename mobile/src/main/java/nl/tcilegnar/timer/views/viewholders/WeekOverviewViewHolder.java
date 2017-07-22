package nl.tcilegnar.timer.views.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.enums.WeekOverviewItem;

public class WeekOverviewViewHolder extends BaseViewHolder<WeekOverviewItem> {
    private TextView dayOfWeekLabel;
    private TextView dayOfWeekValue;

    public WeekOverviewViewHolder(Context activityContext) {
        super(activityContext);
        initViews();
    }

    public WeekOverviewViewHolder(Context activityContext, AttributeSet attrs) {
        super(activityContext, attrs);
        initViews();
    }

    private void initViews() {
        dayOfWeekLabel = (TextView) findViewById(R.id.day_of_week_label);
        dayOfWeekValue = (TextView) findViewById(R.id.total_time);
    }

    @Override
    protected int getViewHolderResource() {
        return R.layout.viewholder_week_overview;
    }

    @Override
    public void loadData(WeekOverviewItem weekOverviewItem) {
        dayOfWeekLabel.setText(weekOverviewItem.getDayOfWeekText());
        dayOfWeekValue.setText(weekOverviewItem.getTotalTimeString());
    }
}
