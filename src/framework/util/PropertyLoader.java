package framework.util;
import java.io.IOException;

import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * Stores the configurable properties required by the framework and tests.
 */
public class PropertyLoader {

    private static Properties props = new Properties();

    /**
     * Store a set of properties from the stream provided.
     * @param is An InputStream with the properties
     * @param importAll Set to false to only import those already defined
     * @throws IOException
     */
    public static void loadProperties(InputStream is, boolean importAll) throws IOException {
        Properties tempProps = new Properties();
        tempProps.load(is);
        loadProperties(tempProps, importAll);
    }

    /**
     * Store a set of properties from the system.
     * @param importAll Set to false to only import those already defined
     */
    public static void loadSystemProperties(boolean importAll) {
        Properties systemProps = System.getProperties();
        loadProperties(systemProps, importAll);
    }

    /**
     * Store the environment variables as a set of properties.
     * @param importAll Set to false to only import those already defined
     */
    public static void loadEnvironmentVariables(boolean importAll) {
        Properties envProps = new Properties();
        envProps.putAll(System.getenv());
        loadProperties(envProps, importAll);
    }

    /**
     * Get a stored property by name.
     * @param key The name of the property
     * @return The value if found, else null
     */
    public static String getProperty(String key) {
        return props.getProperty(key.toLowerCase());
    }

    /**
     * Stores a property with the given name and value.
     * @param key The name of the property
     * @param value The value of the property
     */
    public static void setProperty(String key, String value) {
        props.setProperty(key.toLowerCase(), value);
    }

    /**
     * Gets a string formatted with all the stored properties.
     * @return The formatted string
     */
    public static String dumpProperties() {
        Enumeration<?> e = getSortedKeys();
        StringBuilder sb = new StringBuilder();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            sb.append(key + " = " + props.getProperty(key) + "\n");
        }
        return sb.toString();
    }

    /**
     * Returns a sorted enumeration of all the stored property keys.
     * @return An Enumeration of keys
     */
    private static Enumeration<String> getSortedKeys() {
        Enumeration<?> keysEnum = props.keys();
        Vector<String> keyList = new Vector<String>();
        while (keysEnum.hasMoreElements()) {
            keyList.add((String) keysEnum.nextElement());
        }
        Collections.sort(keyList);
        return keyList.elements();
    }

    /**
     * Imports a new set of properties into the stored properties.
     * @param newProps The new set of properties to possibly import
     * @param importAll Set to false to only import those already defined
     */
    private static void loadProperties(Properties newProps, boolean importAll) {
        Enumeration<?> e = newProps.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            // Store all keys in lowercase form to avoid conflicts
            String newKey = key.toLowerCase();
            if (importAll || props.getProperty(newKey) != null) {
                props.put(newKey, newProps.getProperty(key));
            }
        }
    }

}
