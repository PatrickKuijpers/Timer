package nl.tcilegnar.timer.views.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.models.WeekOfYear;

public class YearOverviewViewHolder extends BaseViewHolder<WeekOfYear> {
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
    public void loadData(WeekOfYear weekOfYear) {
        dayOfWeekLabel.setText(String.valueOf(weekOfYear.getWeekNumber()));
        dayOfWeekValue.setText(String.valueOf(weekOfYear.getTotalTimeInMinutes()));
    }
}
