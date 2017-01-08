package nl.tcilegnar.timer.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.enums.DayEditorItem;

import static android.view.View.OnClickListener;

public class DayEditorItemView extends LinearLayout implements OnClickListener {

    private final DayEditorItem dayEditorItem;

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
        value.setText("..:..");
    }

    @Override
    public void onClick(View view) {
        currentTime = getCurrentTime();
        setCurrentTimeText(currentTime);
    }

    private Calendar getCurrentTime() {
        Locale locale = Locale.GERMANY;
        return Calendar.getInstance(locale);
    }

    private void setCurrentTimeText(Calendar currentTime) {
        String currentTimeText = formatToCurrentTimeText(currentTime);
        value.setText(currentTimeText);
    }

    private String formatToCurrentTimeText(Calendar currentTime) {
        int minute = currentTime.get(Calendar.MINUTE);
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        return hour + ":" + minute;
    }
}
