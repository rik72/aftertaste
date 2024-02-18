package io.rik72.brew.engine.settings.parse;

import io.rik72.brew.engine.settings.parse.registry.ParseTypedRegistry;
import io.rik72.vati.ParseType;
import io.rik72.vati.VatiLocale;

public class CurrentParseType {
	public static ParseType type;

	static {
		setType(VatiLocale.getDefault().getParseType());
	}

	public static void setType(ParseType type) {
		CurrentParseType.type = type;
		ParseTypedRegistry.get().setAll();
	}
}
