package io.rik72.brew.engine.loader.loaders.parsing.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.IllegalParseException;
import io.rik72.brew.engine.loader.loaders.parsing.raw.PossibilityRaw;

public class Possibility extends Parser {

	public String verb;
	public Boolean possible;
	public Boolean important;
	public String inherit;
	public String feedback;

	private Possibility(String verb, Boolean possible, Boolean important, String inherit, String feedback) {
		this.verb = verb;
		this.possible = possible;
		this.important = important;
		this.inherit = inherit;
		this.feedback = feedback;
	}

	// fly ( The ceiling is very low. )
	public static Pattern possibilityP = pattern("word *([text])? *(!important)?");
	public static String possibilityHR = "verb [ (feedback) ]? [ !important ]?";
	// ( initial ) fly
	public static Pattern inheritP = pattern("[word] *word");
	public static String inheritHR = "(status) verb";

	public static Possibility parse(PossibilityRaw raw) {

		String verb;
		Boolean important;
		String inherit;
		String feedback;

		if (raw.impossible != null) {
			Matcher m = possibilityP.matcher(raw.impossible);
			if (m.matches()) {
				verb = trim(m.group(1));
				feedback = trim(m.group(3));
				important = m.group(4) != null;
			}
			else
				throw new IllegalParseException("possibility", raw.impossible, possibilityHR);
			return new Possibility(verb, false, important, null, feedback);
		}

		if (raw.possible != null) {
			Matcher m = possibilityP.matcher(raw.possible);
			if (m.matches()) {
				verb = trim( m.group(1));
				feedback = trim(m.group(3));
				important = m.group(4) != null;
			}
			else
				throw new IllegalParseException("possibility", raw.possible, possibilityHR);
			return new Possibility(verb, true, important, null, feedback);
		}

		if (raw.inherit != null) {
			Matcher m = inheritP.matcher(raw.inherit);
			if (m.matches()) {
				inherit = trim(m.group(1));
				verb = trim(m.group(2));
			}
			else
				throw new IllegalParseException("inherit", raw.inherit, inheritHR);
			return new Possibility(verb, null, null, inherit, null);
		}
		
		return null;
	}
}