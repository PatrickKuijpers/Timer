package nl.tcilegnar.timer.views.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.models.WeekOfYear;
import nl.tcilegnar.timer.utils.TimerCalendarUtil;

public class YearOverviewViewHolder extends BaseViewHolder<WeekOfYear> {
    private TextView weekOfYearView;
    private TextView firstAndLastDayOfWeek;
    private TextView totalTimeView;

    public YearOverviewViewHolder(Context activityContext) {
        super(activityContext);
        initViews();
    }

    public YearOverviewViewHolder(Context activityContext, AttributeSet attrs) {
        super(activityContext, attrs);
        initViews();
    }

    private void initViews() {
        weekOfYearView = (TextView) findViewById(R.id.week_of_year);
        firstAndLastDayOfWeek = (TextView) findViewById(R.id.first_and_last_day_of_week);
        totalTimeView = (TextView) findViewById(R.id.total_time);
    }

    @Override
    protected int getViewHolderResource() {
        return R.layout.viewholder_year_overview;
    }

    @Override
    public void loadData(WeekOfYear weekOfYear) {
        weekOfYearView.setText(String.valueOf(weekOfYear.getWeekNumber()));
        totalTimeView.setText(getTotalTimeText(weekOfYear));
        firstAndLastDayOfWeek.setText(weekOfYear.getReadablePeriodOfWeek());
    }

    private String getTotalTimeText(WeekOfYear weekOfYear) {
        int totalTimeInMinutes = weekOfYear.getTotalTimeInMinutes();
        if (totalTimeInMinutes > 0) {
            return TimerCalendarUtil.getReadableTimeStringHoursAndMinutes(totalTimeInMinutes);
        } else {
            return "-";
        }
    }
}
