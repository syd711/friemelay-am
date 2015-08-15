package de.friemelay.am.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.File;

/**
 *
 */
public class Config {
  
  private static PropertiesConfiguration config;
  
  static {
    try {
      config = new PropertiesConfiguration(new File("conf/settings.properties"));
      config.load();
    } catch (ConfigurationException e) {
      Logger.getLogger(Config.class.getName()).error("Failed to load settings: " + e.getMessage(), e);
    }
  }

  public static Boolean getBoolean(String key) {
    return config.getBoolean(key);
  }
  public static String getString(String key) {
    return config.getString(key);
  }
}
