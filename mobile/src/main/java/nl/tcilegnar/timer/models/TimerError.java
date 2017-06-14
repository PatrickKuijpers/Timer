package nl.tcilegnar.timer.models;

public class TimerError {
    private static final String UNKNOWN_ERROR_MESSAGE = "UNKNOWN_ERROR_MESSAGE";

    private final Throwable throwable;
    private final String message;

    public TimerError() {
        this(UNKNOWN_ERROR_MESSAGE);
    }

    public TimerError(String message) {
        this(message, null);
    }

    public TimerError(Throwable throwable) {
        this(throwable.getMessage(), throwable);
    }

    public TimerError(String message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void throwMe() throws Throwable {
        throw throwable;
    }
}
