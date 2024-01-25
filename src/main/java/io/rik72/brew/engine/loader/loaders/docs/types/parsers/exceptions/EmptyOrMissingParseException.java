package io.rik72.brew.engine.loader.loaders.docs.types.parsers.exceptions;

public class EmptyOrMissingParseException extends ParseException {
	
	public EmptyOrMissingParseException(String entityLabel) {
		super("Empty or missing", entityLabel, null);
	}
}
