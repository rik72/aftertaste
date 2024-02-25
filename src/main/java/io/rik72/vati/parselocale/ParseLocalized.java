package io.rik72.vati.parselocale;

import java.util.Map;

import io.rik72.vati.core.VatiLocale;

public class ParseLocalized <T> {
	private String uniqueId;
	private T value;

	private final Map<ParseLocale, T> valueMap;

	public ParseLocalized(String uniqueId, T asciiValue, T japaneseValue) {
		this.uniqueId = uniqueId;
		this.valueMap = Map.of(
			ParseLocale.ASCII,		asciiValue, 
			ParseLocale.JAPANESE,	japaneseValue);
		ParseLocaleBackend.get().register(this);
	}

	protected void set() {
		value = valueMap.get(VatiLocale.getCurrent().getParseLocale());
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public T get() {
		return value;
	}

	public T get(ParseLocale type) {
		return valueMap.get(type);
	}

	public String toString() {
		return value.toString();
	}
}
