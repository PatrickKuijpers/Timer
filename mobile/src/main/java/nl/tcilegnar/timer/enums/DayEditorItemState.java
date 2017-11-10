package nl.tcilegnar.timer.enums;

import android.support.annotation.StringRes;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.utils.Res;

/** Order of enums should not be changed (due to calculations in for-loops etc)! */
public enum DayEditorItemState {
    Start(R.string.day_editor_item_start),
    BreakStart(R.string.day_editor_item_break_start),
    BreakEnd(R.string.day_editor_item_break_end),
    End(R.string.day_editor_item_end);

    private final String name;

    DayEditorItemState(@StringRes int nameResId) {
        this.name = Res.getString(nameResId);
    }

    @Override
    public String toString() {
        return name;
    }
}
