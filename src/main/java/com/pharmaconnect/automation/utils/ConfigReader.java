package com.pharmaconnect.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        try{
            String projectRoot = System.getProperty("user.dir");
            String configPath = projectRoot + "/src/test/resources/config.properties";
            FileInputStream fis = new FileInputStream(configPath);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        }
        catch (Exception e){
            throw new RuntimeException("Config file not found");
        }
    }

    public static String getProperty(String key){
        String value = properties.getProperty(key);
        if(key == null){
            throw new RuntimeException("Property does not exist");
        }
        else{
            return value;
        }
    }
}
