package nl.tcilegnar.timer.views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.enums.DayEditorItem;

public class DayEditorItemView extends LinearLayout {

    private final DayEditorItem dayEditorItem;

    private TextView label;
    private TextView value;

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
    }

    private void initValues(DayEditorItem dayEditorItem) {
        label.setText(dayEditorItem.name());
        value.setText("00:00");
    }
}
