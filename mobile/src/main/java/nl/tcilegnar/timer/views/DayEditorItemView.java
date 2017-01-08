package nl.tcilegnar.timer.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.utils.storage.Storage;

import static android.view.View.OnClickListener;

public class DayEditorItemView extends LinearLayout implements OnClickListener {

    public static final Locale LOCALE = Locale.GERMANY;
    private final DayEditorItem dayEditorItem;
    private final Storage storage = new Storage();

    private TextView label;
    private TextView value;
    private Calendar currentTime;

    public DayEditorItemView(Context activityContext, DayEditorItem dayEditorItem) {
        super(activityContext);
        inflate(activityContext, R.layout.day_editor_item_view, this);
        this.dayEditorItem = dayEditorItem;

        initViews();
        initValues(dayEditorItem);
    }

    private void initViews() {
        label = (TextView) findViewById(R.id.day_editor_item_label);
        value = (TextView) findViewById(R.id.day_editor_item_value);

        setOnClickListener(this);
    }

    private void initValues(DayEditorItem dayEditorItem) {
        label.setText(dayEditorItem.name());
        int hour = storage.loadDayEditorHour(dayEditorItem.getDayEditorHourKey());
        int minute = storage.loadDayEditorMinute(dayEditorItem.getDayEditorMinuteKey());
        setCurrentTimeText(hour, minute);
    }

    @Override
    public void onClick(View view) {
        currentTime = getCurrentTime();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        setCurrentTimeText(hour, minute);
        saveValues(hour, minute);
    }

    private Calendar getCurrentTime() {
        return Calendar.getInstance(LOCALE);
    }

    private void setCurrentTimeText(int hour, int minute) {
        String currentTimeText = formatToCurrentTimeText(hour, minute);
        value.setText(currentTimeText);
    }

    private String formatToCurrentTimeText(int hour, int minute) {
        String hourText = String.format(LOCALE, "%02d", hour);
        String minuteText = String.format(LOCALE, "%02d", minute);
        return hourText + ":" + minuteText;
    }

    private void saveValues(int hour, int minute) {
        storage.saveDayEditorHour(dayEditorItem.getDayEditorHourKey(), hour);
        storage.saveDayEditorMinute(dayEditorItem.getDayEditorMinuteKey(), minute);
    }
}
