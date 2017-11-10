package nl.tcilegnar.timer.utils.storage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nl.tcilegnar.timer.App;
import nl.tcilegnar.timer.BuildConfig;
import nl.tcilegnar.timer.R;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@SuppressWarnings("all")
public class SharedPrefsTest {
    public static final String SHARED_PREFS_STUB_FILE_NAME = "SharedPrefsStubFileName";
    private SharedPrefsStub sharedPrefsStub;

    @Before
    public void setUp() {
        // Gebruikt om abstract SharedPrefs te kunnen instantiëren
        sharedPrefsStub = new SharedPrefsStub();
    }

    @Test
    public void getPrefs_AbstractFileName_IsExtendedPrefs() {
        // Arrange

        // Act
        SharedPreferences prefs = sharedPrefsStub.getPrefs();

        // Assert
        SharedPreferences extendedPrefs = App.getContext().getSharedPreferences(sharedPrefsStub.fileName(), Context
                .MODE_PRIVATE);
        assertEquals(extendedPrefs, prefs);
    }

    @Test
    public void getPrefs_AbstractFileName_IsNotDefaultPrefs() {
        // Arrange

        // Act
        SharedPreferences prefs = sharedPrefsStub.getPrefs();

        // Assert
        SharedPreferences defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        assertNotSame(defaultSharedPrefs, prefs);
    }

    @Test
    public void getPrefs_FileNameIsNull_IsDefaultPrefs() {
        // Arrange
        SharedPrefsStub sharedPrefsStubSpy = spy(new SharedPrefsStub());
        doReturn(null).when(sharedPrefsStubSpy).fileName();
        doCallRealMethod().when(sharedPrefsStubSpy).getPrefs();

        // Act
        SharedPreferences prefs = sharedPrefsStubSpy.getPrefs();

        // Assert
        SharedPreferences defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        assertEquals(defaultSharedPrefs, prefs);
    }

    @Test
    public void getPrefs_FileNameIsNull_IsNotExtendedPrefs() {
        // Arrange
        SharedPrefsStub sharedPrefsStubSpy = spy(new SharedPrefsStub());
        doReturn(null).when(sharedPrefsStubSpy).fileName();
        doCallRealMethod().when(sharedPrefsStubSpy).getPrefs();

        // Act
        SharedPreferences prefs = sharedPrefsStubSpy.getPrefs();

        // Assert
        SharedPreferences extendedPrefs = App.getContext().getSharedPreferences(this.sharedPrefsStub.fileName(),
                Context.MODE_PRIVATE);
        assertNotSame(extendedPrefs, prefs);
    }

    @Test
    public void saveAndLoadBoolean_True_ValueSaved() {
        // Arrange
        String key = "testKey1";
        boolean expectedSavedValue = true;

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        boolean savedValue = sharedPrefsStub.loadBoolean(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void saveAndLoadBoolean_False_ValueSaved() {
        // Arrange
        String key = "testKey2";
        boolean expectedSavedValue = false;

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        boolean savedValue = sharedPrefsStub.loadBoolean(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void loadBoolean_Default() {
        // Arrange
        String key = "testKey3";
        boolean expectedDefaultValue = false;

        // Act
        boolean defaultValue = sharedPrefsStub.loadBoolean(key);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void loadBoolean_DefaultOverride() {
        // Arrange
        String key = "testKey4";
        boolean expectedDefaultValue = true;

        // Act
        boolean defaultValue = sharedPrefsStub.loadBoolean(key, expectedDefaultValue);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void saveAndLoadString_x_ValueSaved() {
        // Arrange
        String key = "testKey1";
        String expectedSavedValue = "testValue";

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        String savedValue = sharedPrefsStub.loadString(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void saveAndLoadString_Null_ValueSaved() {
        // Arrange
        String key = "testKey2";
        String expectedSavedValue = null;

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        String savedValue = sharedPrefsStub.loadString(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void loadString_Default() {
        // Arrange
        String key = "testKey3";
        String expectedDefaultValue = null;

        // Act
        String defaultValue = sharedPrefsStub.loadString(key);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void loadString_DefaultOverride() {
        // Arrange
        String key = "testKey4";
        String expectedDefaultValue = "defaultValue";

        // Act
        String defaultValue = sharedPrefsStub.loadString(key, expectedDefaultValue);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void saveAndLoadInt_12_ValueSaved() {
        // Arrange
        String key = "testKey1";
        int expectedSavedValue = 12;

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        int savedValue = sharedPrefsStub.loadInt(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void saveAndLoadInt_Minus12_ValueSaved() {
        // Arrange
        String key = "testKey2";
        int expectedSavedValue = -12;

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        int savedValue = sharedPrefsStub.loadInt(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void loadInt_Default() {
        // Arrange
        String key = "testKey3";
        int expectedDefaultValue = 0;

        // Act
        int defaultValue = sharedPrefsStub.loadInt(key);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void loadInt_DefaultOverride() {
        // Arrange
        String key = "testKey4";
        int expectedDefaultValue = 12;

        // Act
        int defaultValue = sharedPrefsStub.loadInt(key, expectedDefaultValue);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void saveAndLoadFloat_1p2_ValueSaved() {
        // Arrange
        String key = "testKey1";
        float expectedSavedValue = 1.2F;

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        float savedValue = sharedPrefsStub.loadFloat(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void saveAndLoadFloat_Minus1p2_ValueSaved() {
        // Arrange
        String key = "testKey2";
        float expectedSavedValue = -1.2F;

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        float savedValue = sharedPrefsStub.loadFloat(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void loadFloat_Default() {
        // Arrange
        String key = "testKey3";
        float expectedDefaultValue = 0;

        // Act
        float defaultValue = sharedPrefsStub.loadFloat(key);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void loadFloat_DefaultOverride() {
        // Arrange
        String key = "testKey4";
        float expectedDefaultValue = 1.2F;

        // Act
        float defaultValue = sharedPrefsStub.loadFloat(key, expectedDefaultValue);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void saveAndLoadLong_123_ValueSaved() {
        // Arrange
        String key = "testKey1";
        long expectedSavedValue = 123;

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        long savedValue = sharedPrefsStub.loadLong(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void saveAndLoadLong_Minus123_ValueSaved() {
        // Arrange
        String key = "testKey2";
        long expectedSavedValue = -123;

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        long savedValue = sharedPrefsStub.loadLong(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void loadLong_Default() {
        // Arrange
        String key = "testKey3";
        long expectedDefaultValue = 0;

        // Act
        long defaultValue = sharedPrefsStub.loadLong(key);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void loadLong_DefaultOverride() {
        // Arrange
        String key = "testKey4";
        long expectedDefaultValue = 123;

        // Act
        long defaultValue = sharedPrefsStub.loadLong(key, expectedDefaultValue);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void saveAndLoadStringSet_NoStrings_ValueSaved() {
        // Arrange
        String key = "testKey1";
        Set<String> expectedSavedValue = new HashSet<>();

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        Set<String> savedValue = sharedPrefsStub.loadStringSet(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void saveAndLoadStringSet_SeveralString_ValueSaved() {
        // Arrange
        String key = "testKey2";
        Set<String> expectedSavedValue = new HashSet<>(Arrays.asList("T", "E", "S", "T"));

        // Act
        sharedPrefsStub.save(key, expectedSavedValue);
        Set<String> savedValue = sharedPrefsStub.loadStringSet(key);

        // Assert
        assertEquals(expectedSavedValue, savedValue);
    }

    @Test
    public void loadStringSet_Default() {
        // Arrange
        String key = "testKey3";
        Set<String> expectedDefaultValue = null;

        // Act
        Set<String> defaultValue = sharedPrefsStub.loadStringSet(key);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void loadStringSet_DefaultOverride() {
        // Arrange
        String key = "testKey4";
        Set<String> expectedDefaultValue = new HashSet<>(Arrays.asList("T", "E", "S", "T"));

        // Act
        Set<String> defaultValue = sharedPrefsStub.loadStringSet(key, expectedDefaultValue);

        // Assert
        assertEquals(expectedDefaultValue, defaultValue);
    }

    @Test
    public void getKey() {
        // Arrange
        String expectedKey = App.getContext().getString(R.string.app_name);

        // Act
        String key = sharedPrefsStub.getKey(R.string.app_name);

        // Assert
        assertEquals(expectedKey, key);
    }

    @Test(expected = Resources.NotFoundException.class)
    public void getKey_NoStringResourceId_ResourcesNotFoundException() {
        // Arrange

        // Act
        sharedPrefsStub.getString(0);

        // Assert
    }

    @Test
    public void getString() {
        // Arrange
        String expectedString = App.getContext().getString(R.string.app_name);

        // Act
        String string = sharedPrefsStub.getString(R.string.app_name);

        // Assert
        assertEquals(expectedString, string);
    }

    @Test(expected = Resources.NotFoundException.class)
    public void getString_NoStringResourceId_ResourcesNotFoundException() {
        // Arrange

        // Act
        sharedPrefsStub.getString(0);

        // Assert
    }

    public class SharedPrefsStub extends SharedPrefs {
        @Override
        protected String fileName() {
            return SHARED_PREFS_STUB_FILE_NAME;
        }
    }
}