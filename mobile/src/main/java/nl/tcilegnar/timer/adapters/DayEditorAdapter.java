package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.views.DayEditorItemView;

public class DayEditorAdapter extends BaseAdapter {
    private final Context activityContext;

    public DayEditorAdapter(Context activityContext) {
        this.activityContext = activityContext;
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
            dayEditorItemView = new DayEditorItemView(activityContext, dayEditorItem);
        } else {
            dayEditorItemView = (DayEditorItemView) convertView;
        }
        return dayEditorItemView;
    }

    private DayEditorItem[] getDayEditorList() {
        return DayEditorItem.values();
    }
}
