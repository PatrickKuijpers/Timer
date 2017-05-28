package nl.tcilegnar.timer.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import nl.tcilegnar.timer.R;
import nl.tcilegnar.timer.utils.Res;

public abstract class ValidationErrorDialogFragment extends DialogFragment {
    private String titleText;
    private String messageText;
    private String positiveButtonText;

    public ValidationErrorDialogFragment() {
        super();
        titleText = Res.getString(R.string.invalid_input);
        messageText = Res.getString(getMessageResId());
        positiveButtonText = Res.getString(R.string.ok);
    }

    protected abstract
    @StringRes
    int getMessageResId();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titleText);
        builder.setMessage(messageText);
        builder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();
    }

    public void show(FragmentActivity activity) {
        String tag = this.getClass().getSimpleName();
        if (activity.getSupportFragmentManager().findFragmentByTag(tag) == null) {
            show(activity.getSupportFragmentManager(), tag);
        }
    }
}