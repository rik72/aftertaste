package io.rik72.brew.engine.loader.loaders.docs.types.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rik72.brew.engine.loader.loaders.docs.types.parsers.exceptions.IllegalParseException;

public class Direction extends Parser {

	public String location;
	public List<String> verbs;
		
	public Direction(String location, List<String> verbs) {
		this.location = location;
		this.verbs = verbs;
	}

	// tower ( fly )
	public static Pattern directionP = pattern("word *([word])?");
	public static String directionHR = "location [ (verb) ]?";

	public static Direction parse(String raw) {

		String location;
		String verb;
		
		if (raw != null) {
			Matcher m = directionP.matcher(raw);
			if (m.matches()) {
				location = trim(m.group(1));
				verb = trim(m.group(3));
			}
			else
				throw new IllegalParseException("direction", raw, directionHR);
			List<String> verbs = new ArrayList<>();
			verbs.add(verb);
			return new Direction(location, verbs);
		}
		
		return null;
	}
}