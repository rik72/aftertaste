package io.rik72.brew.engine.processing.execution.actions.zero;

import io.rik72.brew.engine.db.entities.Character;
import io.rik72.brew.engine.db.entities.Word;
import io.rik72.brew.engine.processing.parsing.mapping.WordMap;

public class ZeroActionNortheast extends ZeroActionDirection {

	protected ZeroActionNortheast(WordMap wordMap, boolean toBeConfirmed, Word verb, Character subject, String additionalFeedback) {
		super(wordMap, toBeConfirmed, verb, subject, additionalFeedback);
	}
}
