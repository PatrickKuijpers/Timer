package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.views.DayEditorItemView;

import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorAdapter extends BaseAdapter {
    private final Context activityContext;
    private final TimePickerDialogListener timePickerDialogListener;

    public DayEditorAdapter(Context activityContext, TimePickerDialogListener timePickerDialogListener) {
        this.activityContext = activityContext;
        this.timePickerDialogListener = timePickerDialogListener;
    }

    @Override
    public int getCount() {
        return getDayEditorList().length;
    }

    @Override
    public DayEditorItem getItem(int i) {
        return getDayEditorList()[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public DayEditorItemView getView(int position, View convertView, ViewGroup viewGroup) {
        DayEditorItemView dayEditorItemView;

        if (convertView == null) {
            DayEditorItem dayEditorItem = getDayEditorList()[position];
            dayEditorItemView = new DayEditorItemView(activityContext, dayEditorItem, timePickerDialogListener);
        } else {
            dayEditorItemView = (DayEditorItemView) convertView;
        }
        return dayEditorItemView;
    }

    private DayEditorItem[] getDayEditorList() {
        return DayEditorItem.values();
    }
}
