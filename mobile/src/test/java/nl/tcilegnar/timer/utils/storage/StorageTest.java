package nl.tcilegnar.timer.utils.storage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import nl.tcilegnar.timer.BuildConfig;

import static nl.tcilegnar.timer.utils.storage.Storage.Key;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StorageTest {
    private Storage storage;

    @Before
    public void setUp() {
        storage = new Storage();
    }

    @Test
    public void fileName_NotNull() {
        // Arrange

        // Act
        String fileName = storage.fileName();

        // Assert
        assertNotNull(fileName.isEmpty());
    }

    @Test
    public void fileName_Storage() {
        // Arrange

        // Act
        String fileName = storage.fileName();

        // Assert
        assertEquals("Als de filename veranderd is kunnen gegevens mogelijk niet meer correct worden geladen",
                "storage", fileName);
    }

    @Test
    public void saveAndLoadDayEditorHour() {
        // Arrange
        int expectedValue = 11;
        Key expectedKey = Key.DayEditorStartHour;

        // Act
        storage.saveTodayEditorHour(expectedKey, expectedValue);
        int value = storage.loadTodayEditorHour(expectedKey);

        // Assert
        assertEquals(expectedValue, value);
    }

    @Test
    public void loadDayEditorHour_DefaultValue() {
        // Arrange
        Key expectedKey = Key.DayEditorStartHour;

        // Act
        int value = storage.loadTodayEditorHour(expectedKey);

        // Assert
        assertEquals(expectedKey.defaultValue, value);
    }
}
