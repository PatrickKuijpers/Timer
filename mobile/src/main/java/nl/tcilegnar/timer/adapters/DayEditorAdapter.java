package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.views.DayEditorItemView;

import static nl.tcilegnar.timer.views.DayEditorItemView.TimeChangeListener;
import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorAdapter extends BaseAdapter implements TimeChangeListener {
    private final Context activityContext;
    private final TimePickerDialogListener timePickerDialogListener;

    private ArrayList<DayEditorItemView> allDayEditorItemViews = new ArrayList<>();

    public DayEditorAdapter(Context activityContext, TimePickerDialogListener timePickerDialogListener) {
        this.activityContext = activityContext;
        this.timePickerDialogListener = timePickerDialogListener;

        DayEditorItem dayEditorItemStart = getItem(0);
        DayEditorItem dayEditorItemBreak = getItem(1);
        DayEditorItem dayEditorItemBreakEnd = getItem(2);
        DayEditorItem dayEditorItemEnd = getItem(3);
        dayEditorItemStart.enable();
        dayEditorItemBreak.disable();
        dayEditorItemBreakEnd.disable();
        dayEditorItemEnd.disable();
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
            dayEditorItemView = new DayEditorItemView(activityContext, dayEditorItem, timePickerDialogListener, this);
            allDayEditorItemViews.add(dayEditorItemView);
        } else {
            dayEditorItemView = (DayEditorItemView) convertView;
        }

        return dayEditorItemView;
    }

    private DayEditorItem[] getDayEditorList() {
        return DayEditorItem.values();
    }

    @Override
    public void onTimeChanged(DayEditorItem dayEditorItem) {
        DayEditorItem dayEditorItemStart = getItem(0);
        DayEditorItem dayEditorItemBreak = getItem(1);
        DayEditorItem dayEditorItemBreakEnd = getItem(2);
        DayEditorItem dayEditorItemEnd = getItem(3);
        switch (dayEditorItem) {
            case Start:
                dayEditorItemStart.disable();
                dayEditorItemBreak.enable();
                dayEditorItemBreakEnd.disable();
                dayEditorItemEnd.enable();
                break;
            case Break:
                dayEditorItemStart.disable();
                dayEditorItemBreak.disable();
                dayEditorItemBreakEnd.enable();
                dayEditorItemEnd.disable();
                break;
            case BreakEnd:
                dayEditorItemStart.disable();
                dayEditorItemBreak.enable();
                dayEditorItemBreakEnd.disable();
                dayEditorItemEnd.enable();
                break;
            case End:
                dayEditorItemStart.disable();
                dayEditorItemBreak.disable();
                dayEditorItemBreakEnd.disable();
                dayEditorItemEnd.disable();
                break;
        }

        for (DayEditorItemView dayEditorItemView : allDayEditorItemViews) {
            dayEditorItemView.updateEnabled();
        }
    }
}
