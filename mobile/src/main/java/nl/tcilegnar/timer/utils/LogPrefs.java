package nl.tcilegnar.timer.utils;

public class LogPrefs {
    private static final MyBuildConfig MY_BUILD_CONFIG = new MyBuildConfig();

    public static boolean shouldLog() {
        return !MY_BUILD_CONFIG.isProduction();
    }
}
