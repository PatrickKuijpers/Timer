package nl.tcilegnar.timer.fragments.dialogs;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.models.Validation;

public class SaveErrorDialog extends ValidationErrorDialogFragment {
    public SaveErrorDialog() {
        super();
    }

    public SaveErrorDialog(Validation validation) {
        super(validation);
    }

    public SaveErrorDialog(String errorMessage) {
        super(errorMessage);
    }

    @Override
    protected int getMessageResId() {
        return R.string.save_error_dialog_message;
    }
}
