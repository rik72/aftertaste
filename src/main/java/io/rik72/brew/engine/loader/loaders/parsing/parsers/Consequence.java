package io.rik72.brew.engine.loader.loaders.parsing.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rik72.brew.engine.loader.loaders.parsing.parsers.exceptions.IllegalParseException;
import io.rik72.vati.parselocale.ParseLocalized;

public class Consequence extends Parser {

	public String entity;
	public String before;
	public String after;
	public String to;
	public String feedback;

	private Consequence(String entity, String before, String after, String to, String feedback) {
		this.entity = entity;
		this.before = before;
		this.after = after;
		this.to = to;
		this.feedback = feedback;
	}

	// . -> invisible / ( something ) status -> other status ( some feedback )
	public static ParseLocalized<Pattern> consequenceP =
		pattern("consequenceP", "(\\.|([word]) *word) *(-> *word)? *(=> *word)? *([text])?");
	public static String consequenceHR =
		"( . | (thing) status_before [ -> status_after ]? [ => location_after ]? [ (feedback) ]? )";

	public static Consequence parse(String raw) {

		String entity;
		String canonical;
		String before;
		String after;
		String to;
		String feedback;
		
		if (raw != null) {
			Matcher m = consequenceP.get().matcher(raw);
			if (m.matches()) {
				entity = trim(m.group(1));
				canonical = trim(m.group(3));
				before = trim(m.group(4));
				after = trim(m.group(6));
				to = trim(m.group(8));
				feedback = trim(m.group(10));
			}
			else {
				throw new IllegalParseException("consequence", raw, consequenceHR);
			}
			if (".".equals(entity))
				return new Consequence(null, null, after, to, feedback);
			else
				return new Consequence(canonical, before, after, to, feedback);
		}

		return null;
	}
}