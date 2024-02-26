package io.rik72.aftertaste.config;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import io.rik72.amber.logger.Log;

public class UserPreferences {

    public static String get(String key) {
		return get(key, null);
    }

    public static String get(String key, String defaultValue) {
        return getInstance().getPrefImpl(key, defaultValue);
    }

    public static void put(String key, String value) {
        getInstance().putPrefImpl(key, value);
    }

    public static void clear() {
        try {
            getInstance().clearAllPrefs();
            Log.info("All user preferences cleared");
        } catch (BackingStoreException e) {
            Log.error("It was not possible to reset user preferences due to a BackingStoreException: " + e.getMessage());
        }
    }

    private Preferences prefs;

	private UserPreferences() {
        prefs = Preferences.userNodeForPackage(UserPreferences.class);
	}

    private String getPrefImpl(String key, String defaultValue) {
        return prefs.get(key, defaultValue);
    }

    private void putPrefImpl(String key, String defaultValue) {
        prefs.put(key, defaultValue);
    }

    private void clearAllPrefs() throws BackingStoreException {
        prefs.clear();
    }

	///////////////////////////////////////////////////////////////////////////
	private static UserPreferences instance = new UserPreferences();
	private static UserPreferences getInstance() {
		return instance;
	}
}
