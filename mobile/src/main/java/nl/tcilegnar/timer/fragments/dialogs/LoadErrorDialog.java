package nl.tcilegnar.timer.fragments.dialogs;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.utils.Res;

public class LoadErrorDialog extends ValidationErrorDialogFragment {
    public static final String TITLE = Res.getString(R.string.error_title_dialog_load);

    public LoadErrorDialog() {
        this("");
    }

    public LoadErrorDialog(String errorMessage) {
        super(TITLE, errorMessage);
    }
}
