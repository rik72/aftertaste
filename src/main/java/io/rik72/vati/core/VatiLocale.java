package io.rik72.vati.core;

import java.util.Locale;

import io.rik72.vati.locale.LocaleBackend;
import io.rik72.vati.parselocale.ParseLocale;
import io.rik72.vati.parselocale.ParseLocaleBackend;

public enum VatiLocale {
	ENGLISH		(Locale.ENGLISH, 	ParseLocale.ASCII),
	JAPANESE	(Locale.JAPANESE, 	ParseLocale.JAPANESE),
	;

	private Locale locale;
	private ParseLocale parseLocale;

	private VatiLocale(Locale locale, ParseLocale parseLocale) {
		this.locale = locale;
		this.parseLocale = parseLocale;
	}

	public Locale getLocale() {
		return locale;
	}

	public ParseLocale getParseLocale() {
		return parseLocale;
	}

	private static VatiLocale current;

	static {
		setCurrent(getDefault());
	}

	public static VatiLocale getCurrent() {
		return current;
	}

	public static void setCurrent(VatiLocale current) {
		VatiLocale.current = current;
		LocaleBackend.get().setLocale(current.getLocale());
		ParseLocaleBackend.get().setAll();
	}

	public static VatiLocale getDefault() {
		return ENGLISH;
	}
}
