package io.rik72.brew.engine.settings.parse.registry;

import java.util.Map;

import io.rik72.brew.engine.settings.parse.CurrentParseType;
import io.rik72.vati.ParseType;

public class ParseTypedString {
	private String uniqueId;
	private String value;

	private final Map<ParseType, String> valueMap;

	public ParseTypedString(String uniqueId, String asciiValue, String japaneseValue) {
		this.uniqueId = uniqueId;
		this.valueMap = Map.of(
			ParseType.ASCII,	asciiValue, 
			ParseType.JAPANESE,	japaneseValue);
		ParseTypedRegistry.get().addString(this);
	}

	protected void set() {
		value = valueMap.get(CurrentParseType.type);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public String get() {
		return value;
	}

	public String get(ParseType type) {
		return valueMap.get(type);
	}

	public String toString() {
		return value;
	}
}
