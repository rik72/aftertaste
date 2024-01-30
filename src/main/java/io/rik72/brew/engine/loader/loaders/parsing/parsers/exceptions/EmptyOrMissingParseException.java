package io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions;

public class EmptyOrMissingParseException extends ParseException {
	
	public EmptyOrMissingParseException(String entityLabel) {
		super("Empty or missing", entityLabel, null);
	}
}
