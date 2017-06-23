package nl.tcilegnar.timer.utils;

import nl.tcilegnar.timer.BuildConfig;

public class MyBuildConfig {
    public boolean isProduction() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("release");
    }

    public boolean isTest() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("alpha");
    }

    public boolean isDevelop() {
        return BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug");
    }
}
