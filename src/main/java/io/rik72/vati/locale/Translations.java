package io.rik72.vati.locale;

import java.text.MessageFormat;

public class Translations {

	public static String get(String label, Object... args) {
		String pattern = LocaleBackend.get().getBundle().getString(label);
		return MessageFormat.format(pattern, args);
	}
}
