package nl.tcilegnar.timer.fragments.dialogs;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.models.Validation;

public class SaveErrorDialog extends ValidationErrorDialogFragment {
    public SaveErrorDialog() {
        super(R.string.error_message_save_dialog);
    }

    public SaveErrorDialog(Validation validation) {
        super(validation);
    }

    public SaveErrorDialog(String errorMessage) {
        super(errorMessage);
    }
}
