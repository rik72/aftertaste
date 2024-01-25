package io.rik72.brew.engine.loader.loaders.docs.types.parsers.exceptions;

import io.rik72.brew.engine.utils.TextUtils;

public abstract class ParseException extends IllegalStateException {
	
	protected ParseException(String type, String entityLabel) {
		this(type, entityLabel, null, null);
	}
	
	protected ParseException(String type, String entityLabel, String raw) {
		this(type, entityLabel, raw, null);
	}
	
	protected ParseException(String type, String entityLabel, String raw, String pattern) {
		super(
			"\n\n    " +
			type + " " + entityLabel +
			(raw != null ? " " + TextUtils.quote(raw) : "") + 
			(pattern != null ? " (expected: " + TextUtils.quote(pattern) + ")" : "") +
			"\n");
	}
}
