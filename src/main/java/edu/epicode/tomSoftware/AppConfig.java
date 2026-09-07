package edu.epicode.tomSoftware;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Single access point to the external configuration of the application.
 */
public class AppConfig {

    /**
     * The location of the configuration file on the classpath.
     */
    private static final String CONFIG_RESOURCE = "/config.properties";

    /**
     * The configuration read at class loading time.
     */
    private static final Properties PROPERTIES = load();

    /**
     * Utility class: it must not be instantiated.
     */
    private AppConfig() {
    }

    /**
     * Reads the configuration file from the classpath.
     *
     * @return the loaded properties, empty if the file cannot be read.
     */
    private static Properties load() {

        Properties properties = new Properties();

        try (InputStream in = AppConfig.class.getResourceAsStream(CONFIG_RESOURCE)) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
        }

        return properties;
    }

    /**
     * Reads a textual value.
     *
     * @param key      the name of the property.
     * @param fallback the value used when the property is missing or empty.
     * @return the configured value, or the fallback.
     */
    private static String get(String key, String fallback) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }

    /**
     * Reads a numeric value.
     *
     * @param key      the name of the property.
     * @param fallback the value used when the property is missing or not a number.
     * @return the configured value, or the fallback.
     */
    private static int getInt(String key, int fallback) {
        try {
            return Integer.parseInt(get(key, String.valueOf(fallback)));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /**
     * @return the folder holding the menu files, terminated by a separator.
     */
    public static String menuDirectory() {
        return get("toms.menu.dir", "src/main/java/edu/epicode/tomSoftware/data/menu/");
    }

    /**
     * @return the folder holding the table files, terminated by a separator.
     */
    public static String tablesDirectory() {
        return get("toms.tables.dir", "src/main/java/edu/epicode/tomSoftware/data/tables/");
    }

    /**
     * @return the path of the rotating log file.
     */
    public static String logFile() {
        return get("toms.log.file", "logs/toms.log");
    }

    /**
     * @return the maximum size in bytes of a single log file before it rotates.
     */
    public static int logMaxBytes() {
        return getInt("toms.log.max.bytes", 1048576);
    }

    /**
     * @return how many rotating log files are kept.
     */
    public static int logFileCount() {
        return getInt("toms.log.file.count", 5);
    }

    /**
     * @return the lowest accepted table number.
     */
    public static int minTableId() {
        return getInt("toms.table.id.min", 1);
    }

    /**
     * @return the highest accepted table number.
     */
    public static int maxTableId() {
        return getInt("toms.table.id.max", 999);
    }

    /**
     * @return the lowest accepted number of guests.
     */
    public static int minPeople() {
        return getInt("toms.table.people.min", 1);
    }

    /**
     * @return the highest accepted number of guests.
     */
    public static int maxPeople() {
        return getInt("toms.table.people.max", 50);
    }
}
