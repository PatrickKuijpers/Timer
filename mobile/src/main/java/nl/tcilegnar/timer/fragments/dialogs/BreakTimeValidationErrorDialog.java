package nl.tcilegnar.timer.fragments.dialogs;

import nl.tcilegnar.timer.R;

public class BreakTimeValidationErrorDialog extends ValidationErrorDialogFragment {
    @Override
    protected int getMessageResId() {
        return R.string.validation_error_dialog_message_break_time;
    }
}
