package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import nl.tcilegnar.timer.enums.DayEditorItem;
import nl.tcilegnar.timer.utils.storage.Storage;
import nl.tcilegnar.timer.views.DayEditorItemView;

import static nl.tcilegnar.timer.views.DayEditorItemView.ActiveChangeListener;
import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorAdapter extends BaseAdapter implements ActiveChangeListener {
    private final Context activityContext;
    private final TimePickerDialogListener timePickerDialogListener;

    private ArrayList<DayEditorItemView> allDayEditorItemViews = new ArrayList<>();

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

        DayEditorItem dayEditorItem = getItem(position);
        if (convertView == null) {
            dayEditorItemView = new DayEditorItemView(activityContext, dayEditorItem, timePickerDialogListener, this);
            allDayEditorItemViews.add(dayEditorItemView);

            // Om de een of andere rede worden sommige views meerdere keren geinitialiseerd, dus >=
            boolean allItemsInitiated = allDayEditorItemViews.size() >= getCount();
            if (allItemsInitiated) {
                setActivation();
            }
        } else {
            dayEditorItemView = (DayEditorItemView) convertView;
            //            updateViews();
        }

        return dayEditorItemView;
    }

    private void setActivation() {
        boolean isAnyActive = false;
        for (DayEditorItem dayEditorItem : getDayEditorList()) {
            if (dayEditorItem.isActive()) {
                onActiveChanged(dayEditorItem);
                isAnyActive = true;
                break;
            }
        }

        if (!isAnyActive) {
            onActiveChanged(null);
        }
    }

    @Override
    public void onActiveChanged(DayEditorItem dayEditorItem) {
        DayEditorItem start = getDayEditorItemStart();
        DayEditorItem breakStart = getDayEditorItemBreakStart();
        DayEditorItem breakEnd = getDayEditorItemBreakEnd();
        DayEditorItem end = getDayEditorItemEnd();
        if (dayEditorItem == null) {
            start.enable();
            breakStart.disable();
            breakEnd.disable();
            end.disable();
        } else {
            switch (dayEditorItem) {
                case Start:
                    start.disable();
                    breakStart.enable();
                    breakEnd.disable();
                    end.enable();
                    break;
                case BreakStart:
                    start.disable();
                    breakStart.disable();
                    breakEnd.enable();
                    end.disable();
                    break;
                case BreakEnd:
                    start.disable();
                    breakStart.disable();
                    breakEnd.disable();
                    end.enable();
                    break;
                case End:
                    start.disable();
                    breakStart.disable();
                    breakEnd.disable();
                    end.disable();
                    break;
            }
        }
        updateViews();
    }

    private void updateViews() {
        for (DayEditorItemView dayEditorItemView : allDayEditorItemViews) {
            dayEditorItemView.updateEnabledViews();
        }
    }

    private DayEditorItem getDayEditorItemStart() {
        return getItem(0);
    }

    private DayEditorItem getDayEditorItemBreakStart() {
        return getItem(1);
    }

    private DayEditorItem getDayEditorItemBreakEnd() {
        return getItem(2);
    }

    private DayEditorItem getDayEditorItemEnd() {
        return getItem(3);
    }

    private DayEditorItem[] getDayEditorList() {
        return DayEditorItem.values();
    }

    public void reset() {
        new Storage().deleteActiveDayEditor();
        onActiveChanged(null);

        for (DayEditorItemView dayEditorItemView : allDayEditorItemViews) {
            dayEditorItemView.resetTime();
        }
    }
}
