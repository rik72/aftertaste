package io.rik72.brew.engine.loader.loaders.parsing.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneAction extends Parser {

	public String verb;
	public String feedback;

	public OneAction(String verb, String feedback) {
		this.verb = verb;
		this.feedback = feedback;
	}

	// take . ( Possessing this item makes you feel more literate. )
	public static Pattern oneActionP = pattern("word *\\. *([text])?");
	public static String oneActionHR = "verb . [ (feedback) ]?";

	public static OneAction parse(String raw) {

		String verb;
		String feedback;

		Matcher m = oneActionP.matcher(raw);
		if (m.matches()) {
			verb = trim( m.group(1));
			feedback = trim(m.group(3));
		}
		else {
			return null;
		}
			
		return new OneAction(verb, feedback);
	}
}