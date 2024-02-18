package io.rik72.brew.engine.settings.parse.registry;

import java.util.HashMap;
import java.util.Map;

public class ParseTypedRegistry {
	private Map<String, ParseTypedString> stringRegistry = new HashMap<>();
	private Map<String, ParseTypedPattern> patternRegistry = new HashMap<>();

	public void setAll() {
		for (ParseTypedString item : stringRegistry.values())
			item.set();
		for (ParseTypedPattern item : patternRegistry.values())
			item.set();
	}

	public void addString(ParseTypedString item) {
		if (stringRegistry.containsKey(item.getUniqueId()))
			throw new IllegalArgumentException(item.getUniqueId() + " string already exists in registry");

		item.set();
		stringRegistry.put(item.getUniqueId(), item);
	}

	public void addPattern(ParseTypedPattern item) {
		if (patternRegistry.containsKey(item.getUniqueId()))
			throw new IllegalArgumentException(item.getUniqueId() + " pattern already exists in registry");

		item.set();
		patternRegistry.put(item.getUniqueId(), item);
	}

	///////////////////////////////////////////////////////////////////////////
	private static ParseTypedRegistry instance = new ParseTypedRegistry();
	public static ParseTypedRegistry get() {
		return instance;
	}
}
