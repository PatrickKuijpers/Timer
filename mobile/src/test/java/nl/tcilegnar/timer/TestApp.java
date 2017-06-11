package nl.tcilegnar.timer;

    public class TestApp extends App {
        /** Prevents onCreate() and onTerminate() to call their super (in which {@link com.orm.SugarApp} is initialized) */
        @Override
        protected boolean callSuper() {
            return false;
        }
    }