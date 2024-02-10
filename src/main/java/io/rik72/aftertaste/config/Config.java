package io.rik72.aftertaste.config;

import java.io.IOException;
import java.util.Properties;

import io.rik72.aftertaste.App;

public class Config {

	private Properties prop = new Properties();

	private Config() {
		try {
			prop.load(App.class.getClassLoader().getResourceAsStream("application.properties"));
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String get(String key) {
		return prop.getProperty(key);
	}

	///////////////////////////////////////////////////////////////////////////
	private static Config instance = new Config();
	public static Config get() {
		return instance;
	}

}
