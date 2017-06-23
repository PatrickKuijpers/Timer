package nl.tcilegnar.timer.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import nl.tcilegnar.timer.App;

public class MyProperties {
    public static String getHockeyAppId() throws Exception {
        return getProperty("Timer_HockeyApp_AppId");
    }

    private static String getProperty(String propertyKey) throws Exception {
        String propertyValue = null;
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "app.properties";

            inputStream = App.getContext().getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            propertyValue = prop.getProperty(propertyKey);
        } finally {
            closeInputStream(inputStream);
        }
        return propertyValue;
    }

    private static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Niets aan te doen
            }
        }
    }
}
