package nl.tcilegnar.timer.utils;

public class Objects {

    /** @see java.util.Objects#requireNonNull(Object) */
    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }
}
