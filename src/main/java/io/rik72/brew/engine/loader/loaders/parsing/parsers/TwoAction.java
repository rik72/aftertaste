package io.rik72.brew.engine.loader.loaders.parsing.parsers;

import java.util.regex.Matcher;

import io.rik72.brew.engine.settings.parse.registry.ParseTypedPattern;

public class TwoAction extends Parser {

	public String verb;
	public String preposition;
	public String supplement;
	public String supplementStatus;
	public String feedback;

	public TwoAction(String verb, String preposition, String supplement, String supplementStatus, String feedback) {
		this.verb = verb;
		this.preposition = preposition;
		this.supplement = supplement;
		this.supplementStatus = supplementStatus;
		this.feedback = feedback;
	}

    // put . ( on ) ( toolshed.shelves ) initial ( some optional feedback )
	public static ParseTypedPattern twoActionP = pattern("twoActionP", "word *\\. *[word] *[word] *word *([text])?");
	public static String twoActionHR = "verb . (preposition) (supplement) status [ (feedback) ]?";

	public static TwoAction parse(String raw) {

		String verb;
		String preposition;
		String supplement;
		String supplementStatus;
		String feedback;

		Matcher m = twoActionP.get().matcher(raw);
		if (m.matches()) {
			verb = trim( m.group(1));
			preposition = trim( m.group(2));
			supplement = trim( m.group(3));
			supplementStatus = trim( m.group(4));
			feedback = trim(m.group(6));
		}
		else {
			return null;
		}

		return new TwoAction(verb, preposition, supplement, supplementStatus, feedback);
	}
}