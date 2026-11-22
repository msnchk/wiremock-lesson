package helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyProvider {
    private static final String PROPERTIES_FILE = "credentials.properties";
    private static final Properties PROPERTIES = new Properties();

    static  {
        try (InputStream propertiesInputStream = PropertyProvider.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (propertiesInputStream == null) {
                throw new RuntimeException("Properties file not found: " + PROPERTIES_FILE);
            }
            PROPERTIES.load(propertiesInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    private PropertyProvider() {}

    public static String getProperty(String key) {
        if (!PROPERTIES.containsKey(key)) {
            throw new IllegalArgumentException("Key '" + key + "' is missing in properties file");
        }
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Value for key '" + key + "' is missing or empty in properties file");
        }
        return value;
    }
}



