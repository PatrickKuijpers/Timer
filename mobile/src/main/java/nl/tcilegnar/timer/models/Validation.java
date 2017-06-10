package nl.tcilegnar.timer.models;

import android.support.annotation.StringRes;

import nl.tcilegnar.timer.utils.Res;

public class Validation {
    private static final String NO_ERROR_MESSAGE = "NO_ERROR_MESSAGE";

    private String errorMessage = NO_ERROR_MESSAGE;

    public Validation() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(@StringRes int errorMessageResId) {
        setErrorMessage(Res.getString(errorMessageResId));
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return errorMessage.equals(NO_ERROR_MESSAGE);
    }
}
