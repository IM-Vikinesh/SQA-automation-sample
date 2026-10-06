package utilities;

import java.io.InputStream;
import java.util.Properties;

/*
 * ConfigReader reads settings from config.properties.
 *
 * Instead of hardcoding the URL, browser name, or timeout in multiple
 * Java files, I put them all in one config file. Changing one line
 * in config.properties updates it everywhere.
 *
 * The static block runs when the class is first loaded — it reads
 * the .properties file from the classpath (src/main/resources).
 * I use try-with-resources so the input stream is auto-closed.
 */
public class ConfigReader {

    private static Properties properties;

    // Static initializer: runs once when class is loaded
    static {
        properties = new Properties();
        // getResourceAsStream() loads files from the classpath (resources folder)
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("config.properties not found in classpath!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Returns the value for a given key (e.g., getProperty("url") returns the website URL)
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
