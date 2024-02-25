package io.rik72.vati.parselocale;

import java.util.HashMap;
import java.util.Map;

public class ParseLocaleBackend {
	private Map<Object, ParseLocalized<?>> registry = new HashMap<>();

	public void setAll() {
		for (ParseLocalized<?> item : registry.values())
			item.set();
	}

	public void register(ParseLocalized<?> item) {
		if (registry.containsKey(item.getUniqueId()))
			throw new IllegalArgumentException(item.getUniqueId() + " object already exists in registry");

		item.set();
		registry.put(item.getUniqueId(), item);
	}

	///////////////////////////////////////////////////////////////////////////
	private static ParseLocaleBackend instance = new ParseLocaleBackend();
	public static ParseLocaleBackend get() {
		return instance;
	}
}
