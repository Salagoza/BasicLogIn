package io.muic.ssc.webapp.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationLoader {

    public static ConfigProperties load() {
        String configFilename = "config.properties";
        try (FileInputStream fin = new FileInputStream(configFilename)) {
            Properties prop = new Properties();
            prop.load(fin);
            // get the property value and print it out
            String user = prop.getProperty("user");
            String driverClassName = prop.getProperty("database.driverClassName");
            String connectionUrl = prop.getProperty("database.connectionUrl");
            String username = prop.getProperty("database.username");
            String password = prop.getProperty("database.password");


            return new ConfigProperties.ConfigPropertiesBuilder()
                    .driverClassName(driverClassName)
                    .connectionUrl(connectionUrl)
                    .username(username)
                    .password(password)
                    .build();

        } catch (Exception e) {
            return null;
        }
    }
}
