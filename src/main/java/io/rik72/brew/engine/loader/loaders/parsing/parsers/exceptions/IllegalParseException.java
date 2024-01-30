package io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions;

public class IllegalParseException extends ParseException {
	
	public IllegalParseException(String entityLabel) {
		super("Illegal", entityLabel);
	}
	
	public IllegalParseException(String entityLabel, String raw) {
		super("Illegal", entityLabel, raw);
	}
	
	public IllegalParseException(String entityLabel, String raw, String pattern) {
		super("Illegal", entityLabel, raw, pattern);
	}
}
