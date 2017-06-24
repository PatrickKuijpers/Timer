package nl.tcilegnar.timer.fragments.dialogs;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.utils.Res;

public class SaveErrorDialog extends ValidationErrorDialogFragment {
    public static final String MESSAGE = Res.getString(R.string.error_message_dialog_save);
    public static final String MESSAGE_AND = MESSAGE + Res.getString(R.string.label_seperator);

    public SaveErrorDialog() {
        super(MESSAGE);
    }

    public SaveErrorDialog(String errorMessage) {
        super(MESSAGE_AND + errorMessage);
    }
}
