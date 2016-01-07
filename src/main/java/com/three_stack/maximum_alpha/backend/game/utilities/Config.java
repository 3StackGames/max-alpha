package com.three_stack.maximum_alpha.backend.game.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final String CONFIG_FILE = "development.properties";
    private static Properties properties = new Properties();

    static {
        InputStream input = null;
        try {
            input = Config.class.getClass().getResourceAsStream("/" + CONFIG_FILE);
            properties.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("[Exception] File not found: " + CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[Exception] IO Exception while reading " + CONFIG_FILE);
        }
    }

    public static String getProperty(String property) {
        return properties.getProperty(property);
    }
}
