package io.rik72.vati;

import java.util.Locale;

public enum VatiLocale {
	ENGLISH		(Locale.ENGLISH, 	ParseType.ASCII),
	JAPANESE	(Locale.JAPANESE, 	ParseType.JAPANESE),
	;

	private Locale locale;
	private ParseType parseType;

	private VatiLocale(Locale locale, ParseType parseType) {
		this.locale = locale;
		this.parseType = parseType;
	}

	public Locale getLocale() {
		return locale;
	}

	public ParseType getParseType() {
		return parseType;
	}

	public static VatiLocale getDefault() {
		return ENGLISH;
	}
}
