package nl.tcilegnar.timer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.interfaces.IDayEditorItem;
import nl.tcilegnar.timer.models.DayEditorItem;
import nl.tcilegnar.timer.models.TodayEditorItem;
import nl.tcilegnar.timer.utils.storage.Storage;
import nl.tcilegnar.timer.views.DayEditorItemView;
import nl.tcilegnar.timer.views.DayEditorItemView.DayEditorListener;
import nl.tcilegnar.timer.views.DayEditorItemView.TimeChangedListener;

import static nl.tcilegnar.timer.views.DayEditorItemView.ActiveItemChangeListener;
import static nl.tcilegnar.timer.views.DayEditorItemView.TimePickerDialogListener;

public class DayEditorAdapter extends ArrayAdapter<IDayEditorItem> implements ActiveItemChangeListener {
    private final List<IDayEditorItem> dayEditorItems;
    private final DayEditorListener dayEditorListener;
    private final TimePickerDialogListener timePickerDialogListener;
    private final TimeChangedListener timeChangedListener;

    private ArrayList<DayEditorItemView> allDayEditorItemViews = new ArrayList<>();

    public DayEditorAdapter(Context activityContext, List<IDayEditorItem> dayEditorItems, DayEditorListener
            dayEditorListener, TimePickerDialogListener timePickerDialogListener, TimeChangedListener
            timeChangedListener) {
        super(activityContext, R.layout.day_editor_item_view, dayEditorItems); // TODO: need view here?
        this.dayEditorItems = dayEditorItems;
        this.dayEditorListener = dayEditorListener;
        this.timePickerDialogListener = timePickerDialogListener;
        this.timeChangedListener = timeChangedListener;
    }

    @Override
    @NonNull
    public DayEditorItemView getView(int position, @Nullable View convertView, @NonNull ViewGroup viewGroup) {
        DayEditorItemView dayEditorItemView;
        IDayEditorItem dayEditorItem = getItem(position);
        if (convertView == null) {
            dayEditorItemView = new DayEditorItemView(getContext(), dayEditorItem, dayEditorListener,
                    timePickerDialogListener, timeChangedListener, this);
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
        for (IDayEditorItem dayEditorItem : dayEditorItems) {
            if (dayEditorItem.isActive()) {
                onActiveItemChanged(dayEditorItem);
                isAnyActive = true;
                break;
            }
        }

        if (!isAnyActive) {
            onActiveItemChanged(null);
        }
    }

    // TODO: can probably be improved!
    public void onActiveItemChanged(IDayEditorItem dayEditorItem) {
        if (dayEditorItem == null) {
            initEnabled();
        } else {
            if (dayEditorItem instanceof DayEditorItem) {
                doDayEditorUpdate((DayEditorItem) dayEditorItem);
            } else if (dayEditorItem instanceof TodayEditorItem) {
                doTodayEditorUpdate((TodayEditorItem) dayEditorItem);
            }
        }
        updateViews();
    }

    private void initEnabled() {
        IDayEditorItem start = getDayEditorItemStart();
        IDayEditorItem breakStart = getDayEditorItemBreakStart();
        IDayEditorItem breakEnd = getDayEditorItemBreakEnd();
        IDayEditorItem end = getDayEditorItemEnd();
        start.enable();
        breakStart.disable();
        breakEnd.disable();
        end.disable();
    }

    private void doDayEditorUpdate(DayEditorItem dayEditorItem) {
        IDayEditorItem start = getDayEditorItemStart();
        IDayEditorItem breakStart = getDayEditorItemBreakStart();
        IDayEditorItem breakEnd = getDayEditorItemBreakEnd();
        IDayEditorItem end = getDayEditorItemEnd();
        switch (dayEditorItem.getState()) {
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

    private void doTodayEditorUpdate(TodayEditorItem todayEditorItem) {
        IDayEditorItem start = getDayEditorItemStart();
        IDayEditorItem breakStart = getDayEditorItemBreakStart();
        IDayEditorItem breakEnd = getDayEditorItemBreakEnd();
        IDayEditorItem end = getDayEditorItemEnd();
        switch (todayEditorItem.getState()) {
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

    private void updateViews() {
        for (DayEditorItemView dayEditorItemView : allDayEditorItemViews) {
            dayEditorItemView.updateEnabledViews();
        }
    }

    private IDayEditorItem getDayEditorItemStart() {
        return getItem(0);
    }

    private IDayEditorItem getDayEditorItemBreakStart() {
        return getItem(1);
    }

    private IDayEditorItem getDayEditorItemBreakEnd() {
        return getItem(2);
    }

    private IDayEditorItem getDayEditorItemEnd() {
        return getItem(3);
    }

    public void reset() {
        new Storage().deleteActiveTodayEditor();
        onActiveItemChanged(null);

        for (DayEditorItemView dayEditorItemView : allDayEditorItemViews) {
            dayEditorItemView.resetTime();
        }
    }
}
