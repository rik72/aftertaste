package io.rik72.vati;

import java.text.MessageFormat;
import java.util.Locale;

public class Strings {

	public static String get(String label, Object... args) {
		String pattern = StringsBackend.get().getBundle().getString(label);
		return MessageFormat.format(pattern, args);
	}

	public static void setLocale(Locale locale) {
		StringsBackend.get().setLocale(locale);
	}
}
