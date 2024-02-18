package io.rik72.brew.engine.settings.parse.registry;

import java.util.Map;
import java.util.regex.Pattern;

import io.rik72.brew.engine.settings.parse.CurrentParseType;
import io.rik72.vati.ParseType;

public class ParseTypedPattern {
	private String uniqueId;
	private Pattern value;

	private final Map<ParseType, Pattern> valueMap;

	public ParseTypedPattern(String uniqueId, Pattern asciiValue, Pattern japaneseValue) {
		this.uniqueId = uniqueId;
		this.valueMap = Map.of(
			ParseType.ASCII,	asciiValue, 
			ParseType.JAPANESE,	japaneseValue);
		ParseTypedRegistry.get().addPattern(this);
	}

	protected void set() {
		value = valueMap.get(CurrentParseType.type);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public Pattern get() {
		return value;
	}

	public Pattern get(ParseType type) {
		return valueMap.get(type);
	}
}
