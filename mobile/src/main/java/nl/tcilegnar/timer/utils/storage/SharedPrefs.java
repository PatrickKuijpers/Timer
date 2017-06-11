package nl.tcilegnar.timer.utils.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import java.util.Set;

import nl.tcilegnar.timer.App;

public abstract class SharedPrefs {
    private SharedPreferences extendedSharedPrefs = App.getContext().getSharedPreferences(fileName(), Context
            .MODE_PRIVATE);

    private SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(App.getContext());

    protected abstract String fileName();

    protected SharedPreferences getPrefs() {
        if (fileName() != null) {
            return extendedSharedPrefs;
        } else {
            return defaultPrefs;
        }
    }

    private Editor prefsEdit() {
        return getPrefs().edit();
    }

    // Booleans
    protected void save(String key, boolean value) {
        prefsEdit().putBoolean(key, value).apply();
    }

    protected boolean loadBoolean(String key) {
        return loadBoolean(key, false);
    }

    protected boolean loadBoolean(String key, boolean defaultValue) {
        return getPrefs().getBoolean(key, defaultValue);
    }

    // Strings
    protected void save(String key, String value) {
        prefsEdit().putString(key, value).apply();
    }

    protected String loadString(String key) {
        return loadString(key, null);
    }

    protected String loadString(String key, String defaultValue) {
        return getPrefs().getString(key, defaultValue);
    }

    // Integers
    protected void save(String key, int value) {
        prefsEdit().putInt(key, value).apply();
    }

    protected int loadInt(String key) {
        return loadInt(key, 0);
    }

    protected int loadInt(String key, int defaultValue) {
        return getPrefs().getInt(key, defaultValue);
    }

    // Floats
    protected void save(String key, float value) {
        prefsEdit().putFloat(key, value).apply();
    }

    protected float loadFloat(String key) {
        return loadFloat(key, 0);
    }

    protected float loadFloat(String key, float defaultValue) {
        return getPrefs().getFloat(key, defaultValue);
    }

    // Longs
    protected void save(String key, long value) {
        prefsEdit().putLong(key, value).apply();
    }

    protected long loadLong(String key) {
        return loadLong(key, 0);
    }

    protected long loadLong(String key, long defaultValue) {
        return getPrefs().getLong(key, defaultValue);
    }

    // StringSets
    protected void save(String key, Set<String> value) {
        prefsEdit().putStringSet(key, value).apply();
    }

    protected Set<String> loadStringSet(String key) {
        return loadStringSet(key, null);
    }

    protected Set<String> loadStringSet(String key, Set<String> defaultValue) {
        return getPrefs().getStringSet(key, defaultValue);
    }

    protected String getKey(@StringRes int resId) {
        return getString(resId);
    }

    protected String getString(@StringRes int resId) {
        return App.getContext().getString(resId);
    }
}