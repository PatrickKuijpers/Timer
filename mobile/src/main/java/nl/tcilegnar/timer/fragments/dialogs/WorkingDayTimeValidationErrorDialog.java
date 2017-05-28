package nl.tcilegnar.timer.fragments.dialogs;

import nl.tcilegnar.timer.R;

public class WorkingDayTimeValidationErrorDialog extends ValidationErrorDialogFragment {
    @Override
    protected int getMessageResId() {
        return R.string.validation_error_dialog_message_working_day_time;
    }
}
